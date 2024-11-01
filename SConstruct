import os
import subprocess
import sysconfig
import numpy as np

arch = subprocess.check_output(["uname", "-m"], encoding='utf8').rstrip()

python_path = sysconfig.get_paths()['include']
cpppath = [
  "opendbc/can",
  '/usr/lib/include',
  python_path
]

AddOption('--kaitai',
          action='store_true',
          help='Regenerate kaitai struct parsers')
          
AddOption('--test',
          action='store_true',
          help='build test files')

AddOption('--asan',
          action='store_true',
          help='turn on ASAN')

AddOption('--ubsan',
          action='store_true',
          help='turn on UBSan')

SHARED = False

lenv = {
  "PATH": os.environ['PATH'] + ":" + Dir(f"#libs/capnpc-java/{arch}/bin").abspath,
  "LD_LIBRARY_PATH": [Dir(f"#third_party/acados/{arch}/lib").abspath],
  "PYTHONPATH": Dir("#").abspath + ":" + Dir("#third_party/acados").abspath,

  "ACADOS_SOURCE_DIR": Dir("#third_party/acados/include/acados").abspath,
  "ACADOS_PYTHON_INTERFACE_PATH": Dir("#third_party/acados/acados_template").abspath,
  "TERA_PATH": Dir("#").abspath + f"/third_party/acados/{arch}/t_renderer",
}

libpath = [
      f"#third_party/acados/{arch}/lib",
      ]

cflags = []
cxxflags = []

rpath = lenv["LD_LIBRARY_PATH"].copy()
rpath += ["/usr/local/lib"]

rpath += [
    Dir("#cereal").abspath,
    Dir("#common").abspath
  ]

if GetOption('asan'):
  ccflags = ["-fsanitize=address", "-fno-omit-frame-pointer"]
  ldflags = ["-fsanitize=address"]
elif GetOption('ubsan'):
  ccflags = ["-fsanitize=undefined"]
  ldflags = ["-fsanitize=undefined"]
else:
  ccflags = []
  ldflags = []

lenv = {
  "PATH": "/home/fgx/.buildozer/android/platform/android-ndk-r25b/toolchains/llvm/prebuilt/linux-x86_64/bin/:/home/fgx/p4a2/venv/bin:/bin:/usr/local/bin/", #os.environ['PATH'],
  "LD_LIBRARY_PATH": ["/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/libs_collections/oscservice/arm64-v8a"],
  "PYTHONPATH": Dir("#").abspath + ':' + Dir(f"#third_party/acados").abspath,

  "ACADOS_SOURCE_DIR": Dir("#third_party/acados").abspath,
  "ACADOS_PYTHON_INTERFACE_PATH": Dir("#third_party/acados/acados_template").abspath,
  "TERA_PATH": Dir("#").abspath + f"/third_party/acados/x86_64/t_renderer"
}

cflags = []
cxxflags = []
cpppath = []
rpath = [] #lenv["LD_LIBRARY_PATH"].copy()

libpath = [
  # f"#third_party/acados/{arch}/lib",
  # f"#third_party/libyuv/{arch}/lib",

  # "/usr/lib",
  # "/usr/local/lib",
]


if GetOption('asan'):
  ccflags = ["-fsanitize=address", "-fno-omit-frame-pointer"]
  ldflags = ["-fsanitize=address"]
elif GetOption('ubsan'):
  ccflags = ["-fsanitize=undefined"]
  ldflags = ["-fsanitize=undefined"]
else:
  ccflags = []
  ldflags = ['-llog']

# Enable swaglog include in submodules
cflags += ['-DSWAGLOG="\\"common/swaglog.h\\""']
cxxflags += ['-DSWAGLOG="\\"common/swaglog.h\\""']

# ccflags_option = GetOption('ccflags')
# if ccflags_option:
#   ccflags += ccflags_option.split(' ')

env = Environment(
  ENV=lenv,
  CCFLAGS=[
    "-g",
    "-fPIC",
    "-O2",
    "-Wunused",
    "-Werror",
    "-Wshadow",
    "-Wno-unknown-warning-option",
    "-Wno-deprecated-register",
    "-Wno-register",
    "-Wno-inconsistent-missing-override",
    "-Wno-c99-designator",
    "-Wno-reorder-init-list",
    "-Wno-error=unused-but-set-variable",
    "-Wno-vla-cxx-extension",
  ] + cflags + ccflags,

  CPPPATH=cpppath + [
    "#",
    "#third_party/acados/include",
    "#third_party/acados/include/blasfeo/include",
    "#third_party/acados/include/hpipm/include",
    "#third_party/catch2/include",
    "#third_party/libusb",
    "#third_party/libyuv/include",
    "#third_party/json11",
    "#third_party/opencl/include",
    "#third_party/linux/include",
    "#third_party/snpe/include",
    "#third_party/lmdb",
    "#third_party/qrcode",
    "#third_party/capnproto/c++/src",
    "#third_party",
    "#selfdrive/modeld",
    "#third_party/libOpenCL-loader/include",
    "#cereal",
    "#common",
    "#msgq",
    "#opendbc/can",
    # "/home/fgx/openpilot/third_party",
    # "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/other_builds/capnp/arm64-v8a__ndk_target_24/capnp/c++/src/",
    "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/other_builds/libzmq/arm64-v8a__ndk_target_24/libzmq/include/",
    "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/other_builds/python3/arm64-v8a__ndk_target_24/python3/Include/",
    "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/python-installs/oscservice/arm64-v8a/numpy/core/include/",
    # "/usr/local/include/capnp/",
  ],

  CC='clang --target=aarch64-linux-android24 --sysroot=/home/fgx/.buildozer/android/platform/android-ndk-r25b/toolchains/llvm/prebuilt/linux-x86_64/sysroot',
  CXX='clang++ --target=aarch64-linux-android24 --sysroot=/home/fgx/.buildozer/android/platform/android-ndk-r25b/toolchains/llvm/prebuilt/linux-x86_64/sysroot',
  LINKFLAGS=ldflags,
  AR='llvm-ar',
  RANLIB='llvm-ranlib',
  LD='llvm-ld',

  RPATH=rpath,

  CFLAGS=["-std=gnu11"] + cflags,
  CXXFLAGS=["-std=c++17"] + cxxflags,
  LIBPATH=libpath + [
    "#msgq_repo",
    "#third_party",
    "#third_party/json11",
    "#third_party/libusb/android/libs/arm64-v8a",
    "#third_party/CL",
    "#third_party/lmdb",
    "#selfdrive/pandad",
    "#common",
    "#cereal",
    "#third_party/libOpenCL-loader/projects/android/obj/local/arm64-v8a/",
    "#selfdrive/modeld",
    "#rednose/helpers",
    # "/home/fgx/openpilot/cereal",
    # "/home/fgx/.buildozer/android/platform/android-ndk-r25b/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/lib",
    "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/libs_collections/python/arm64-v8a/",
    "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/other_builds/python3/arm64-v8a__ndk_target_24/python3/android-build",
    "/home/fgx/.buildozer/android/platform/android-ndk-r25b/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/lib/aarch64-linux-android/32",
  ],
  CYTHONCFILESUFFIX=".cpp",
  COMPILATIONDB_USE_ABSPATH=True,
  # REDNOSE_ROOT="#",
  tools=["default", "cython"],
  toolpath = ['opendbc/site_scons/site_tools'],
)

# Cython build enviroment
py_include = "/home/fgx/flowy/.buildozer/android/platform/build-arm64-v8a/build/other_builds/python3/arm64-v8a__ndk_target_24/python3/Include/" # sysconfig.get_paths()['include']
envCython = env.Clone()
envCython["CPPPATH"] += [py_include] #, np.get_include()]
envCython["CCFLAGS"] += ["-Wno-#warnings", "-Wno-shadow", "-Wno-deprecated-declarations"]
envCython["CCFLAGS"].remove("-Werror")

envCython["LIBS"] = ['python3.11']
if arch == "Darwin":
  envCython["LINKFLAGS"] = ["-bundle", "-undefined", "dynamic_lookup"] + darwin_rpath_link_flags
else:
  envCython["LINKFLAGS"] = ["-pthread", "-shared"]

Export('envCython')

QCOM_REPLAY = False
Export('env', 'arch', 'QCOM_REPLAY', 'SHARED')

SConscript(['common/SConscript'])
Import('_common', '_gpucommon')

common = [_common, 'json11', 'lmdb']
gpucommon = _gpucommon

Export('common', 'gpucommon')

envCython = env.Clone()
envCython["CPPPATH"] += [np.get_include()]
envCython["CCFLAGS"] += ["-Wno-#warnings", "-Wno-shadow", "-Wno-deprecated-declarations"]

python_libs = []
if arch == "Darwin":
  envCython["LINKFLAGS"] = ["-bundle", "-undefined", "dynamic_lookup"]
elif arch == "aarch64":
  envCython["LINKFLAGS"] = ["-shared"]

  python_libs.append(os.path.basename(python_path))
else:
  envCython["LINKFLAGS"] = ["-pthread", "-shared"]

envCython["LIBS"] = ['python3.11']

Export('envCython')

# cereal and messaging are shared with the system
SConscript(['cereal/SConscript'])
# if SHARED:
#   cereal = abspath([File('cereal/libcereal_shared.so')])
#   messaging = abspath([File('cereal/libmessaging_shared.so')])
# else:

# Export('cereal', 'messaging')

# # Build rednose library and ekf models

rednose_deps = [
  "#selfdrive/locationd/models/constants.py",
  "#selfdrive/locationd/models/gnss_helpers.py",
]

rednose_config = {
  'generated_folder': '#selfdrive/locationd/models/generated',
  'to_build': {
    'gnss': ('#selfdrive/locationd/models/gnss_kf.py', True, [], rednose_deps),
    'live': ('#selfdrive/locationd/models/live_kf.py', True, ['live_kf_constants.h'], rednose_deps),
    'car': ('#selfdrive/locationd/models/car_kf.py', True, [], rednose_deps),
  },
}

if arch != "larch64":
  rednose_config['to_build'].update({
    'loc_4': ('#selfdrive/locationd/models/loc_kf.py', True, [], rednose_deps),
    'lane': ('#selfdrive/locationd/models/lane_kf.py', True, [], rednose_deps),
    'pos_computer_4': ('#rednose/helpers/lst_sq_computer.py', False, [], []),
    'pos_computer_5': ('#rednose/helpers/lst_sq_computer.py', False, [], []),
    'feature_handler_5': ('#rednose/helpers/feature_handler.py', False, [], []),
  })

Export('rednose_config')
SConscript(['rednose/SConscript'])

SConscript(['third_party/SConscript'])

SConscript([
  'system/clocksd/SConscript',
])

# build submodules
SConscript([
  'opendbc/can/SConscript',
  'panda/SConscript',
])

# SConscript(['SConscript'])

SConscript(['system/proclogd/SConscript'])

SConscript(['common/kalman/SConscript'])
SConscript(['common/transformations/SConscript'])

SConscript(['selfdrive/modeld/SConscript'])

SConscript(['selfdrive/controls/lib/lateral_mpc_lib/SConscript'])
SConscript(['selfdrive/controls/lib/longitudinal_mpc_lib/SConscript'])

# SConscript(['selfdrive/locationd/SConscript'])
SConscript(['selfdrive/boardd/SConscript'])
SConscript(['selfdrive/loggerd/SConscript'])
SConscript(['thneedrunner/SConscript'])

# if GetOption('test'):
#   SConscript('panda/tests/safety/SConscript')
