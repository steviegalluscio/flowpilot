#define PY_SSIZE_T_CLEAN
#include "Python.h"
#include <string>
#include <jni.h>
#include <unistd.h>
#include "android/log.h"
#include <stdio.h>

#define LOG(n, x) __android_log_write(ANDROID_LOG_INFO, (n), (x))
#define LOGP(x) LOG("python", (x))

static PyObject *androidembed_log(PyObject *self, PyObject *args) {
  char *logstr = NULL;
  if (!PyArg_ParseTuple(args, "s", &logstr)) {
    return NULL;
  }
  LOG(getenv("PYTHON_NAME"), logstr);
  Py_RETURN_NONE;
}

static PyMethodDef AndroidEmbedMethods[] = {
    {"log", androidembed_log, METH_VARARGS, "Log on android platform"},
    {NULL, NULL, 0, NULL}};

static struct PyModuleDef androidembed = {PyModuleDef_HEAD_INIT, "androidembed",
                                          "", -1, AndroidEmbedMethods};

PyMODINIT_FUNC initandroidembed(void) {
  return PyModule_Create(&androidembed);
}

extern "C" {
jstring JNICALL Java_ai_flow_flowy_PythonRunner_run(JNIEnv *env, jobject obj, jint fd) {
    std::string home = "/data/data/ai.flow.android/files/app/_python_bundle/";
    std::string path = "/data/data/ai.flow.android/files/app/";
    PyConfig config;
    PyImport_AppendInittab("androidembed", initandroidembed);
    PyConfig_InitPythonConfig(&config);
    config.home = Py_DecodeLocale(home.c_str(), NULL);
    config.pythonpath_env = Py_DecodeLocale(path.c_str(), NULL);
    chdir(path.c_str());
    config.module_search_paths_set = 1;
    PyWideStringList_Append(&config.module_search_paths, L"/data/data/ai.flow.android/files/app");
    PyWideStringList_Append(&config.module_search_paths, L"/data/data/ai.flow.android/files/app/_python_bundle/site-packages");
    PyWideStringList_Append(&config.module_search_paths, L"/data/data/ai.flow.android/files/app/_python_bundle/modules");
    PyWideStringList_Append(&config.module_search_paths, L"/data/data/ai.flow.android/files/app/_python_bundle/stdlib.zip");

    Py_InitializeFromConfig(&config);
    PyRun_SimpleString("import androidembed\nandroidembed.log('testing python "
                     "print redirection')");

    PyRun_SimpleString("import sys");
    PyRun_SimpleString("sys.path = ['/data/data/ai.flow.android/files/app'] + sys.path");

    char runstr[256];
    sprintf(runstr,
        "import traceback\n"
        "import panda\n"
        "androidembed.log('launching with fd %d')\n"
        "try:\n"
        "  p = panda.Panda(fd='%d', bootstub=False)\n"
        "  androidembed.log('PYGOT: ' + str(p.health()))\n"
        "except:\n"
        "  androidembed.log(traceback.format_exc())\n"
    , fd, fd);
    PyRun_SimpleString(runstr);

    PyObject* panda_module = PyImport_ImportModule("panda");
    PyObject* panda_cls = PyObject_GetAttrString(panda_module, "Panda");
    PyObject* args = PyTuple_New(0);
    PyObject* kwargs = PyDict_New();
    PyDict_SetItemString(kwargs, "fd", PyLong_FromLong(fd));
    PyDict_SetItemString(kwargs, "bootstub", Py_False);
    PyObject* panda_instance = PyObject_Call(panda_cls, args, kwargs);

    if (panda_instance == NULL) {
        PyObject *type, *value, *traceback;
        PyErr_Fetch(&type, &value, &traceback);
        PyObject* repr = PyObject_Repr(value);
        Py_Finalize();
        LOGP("panda_instance is null");
        return env->NewStringUTF(PyUnicode_AsUTF8(repr));
    }
    
    PyObject* health_method = PyObject_GetAttrString(panda_instance, "health");
    PyObject* health_result = PyObject_CallNoArgs(health_method);

    if (health_result == NULL) {
        PyObject *type, *value, *traceback;
        PyErr_Fetch(&type, &value, &traceback);
        PyObject* repr = PyObject_Repr(value);
        LOGP("health_result is null");
        Py_Finalize();
        return env->NewStringUTF(PyUnicode_AsUTF8(repr));
    }

    Py_Finalize();
    return env->NewStringUTF(PyUnicode_AsUTF8(health_result));
}
}