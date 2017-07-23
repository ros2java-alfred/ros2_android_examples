# android_prepare.sh
#!/bin/sh

set -ev

DEBUG=1
DOCKER_IMG="$DOCKER_REPO:$DOCKER_DIST"

# Docker Hub authenticate.
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD"
docker pull $DOCKER_IMG

# Make shared environment variables.
cd $HOME_BUILD
env | grep -E '^TRAVIS_' > $HOME_ENV && \
env | grep -E '^ANDROID_' >> $HOME_ENV && \
env | grep -E '^ROS' >> $HOME_ENV && \
env | grep -E '^COVERALLS_' >> $HOME_ENV && \
env | grep -E '^CI_' >> $HOME_ENV && \
echo -e "CI_BUILD_NUMBER=$TRAVIS_BUILD_NUMBER\nCI_PULL_REQUEST=$TRAVIS_PULL_REQUEST\nCI_BRANCH=$TRAVIS_BRANCH" >> $HOME_ENV && \
echo -e "PYTHON_PATH=$PYTHON_PATH\nROOT_PATH=$ROOT_PATH" >> $HOME_ENV

if [ $DEBUG -eq 1 ]
then
# Check container variables.
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) --env-file $HOME_ENV -w $(pwd) $DOCKER_IMG sh -c "locale && env | grep -E '^TRAVIS_' && env | grep -E '^CI_' && env | grep -E '^ROS' && env | grep -E '^ANDROID'"
fi

echo "\e[33mINSTALL ANDROID SDK & NDK..."
if [ ! -d "$ANDROID_NDK_HOME/platforms" ]; then
    cd $HOME_BUILD
    echo "Download ..." && wget https://dl.google.com/android/repository/$ANDROID_NDK_VER-linux-x86_64.zip -nv && echo "Unzip it..." && unzip -q -o $ANDROID_NDK_VER-linux-x86_64.zip -d ./ && echo "Remove zip file." && rm -f $ANDROID_NDK_VER-linux-x86_64.zip
fi

if [ $DEBUG -eq 1 ]
then
  ls -lFa ./
fi

if [ ! -d "$ANDROID_SDK_ROOT/tools" ]
then
  cd $HOME_BUILD
  echo "Download ..." && wget https://dl.google.com/android/repository/$ANDROID_SDK_VER.zip -nv && echo "Unzip it..." && unzip -q -o $ANDROID_SDK_VER.zip -d $ANDROID_SDK_VER && echo "Remove zip file." && rm -f $ANDROID_SDK_VER.zip
fi

if [ $DEBUG -eq 1 ]
then
  ls -lFa ./
fi

if [ ! -d "$ANDROID_SDK_ROOT/licenses" ]
then
  mkdir -p $ANDROID_SDK_ROOT/licenses
  echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_SDK_ROOT/licenses/android-sdk-license"
  echo -e "\n84831b9409646a918e30573bab4c9c91346d8abd" > "$ANDROID_SDK_ROOT/licenses/android-sdk-preview-license"
  echo -e "\n152e8995e4332c0dc80bc63bf01fe3bbccb0804a\nd975f751698a77b662f1254ddbeed3901e976f5a" > "$ANDROID_SDK_ROOT/licenses/intel-android-extra-license"
fi

if [ ! -f "$HOME_BUILD/.android/repositories.cfg" ]
then
  mkdir -p $HOME_BUILD/.android
  echo "### User Sources for Android SDK Manager" >> $HOME_BUILD/.android/repositories.cfg
  echo "count=0" >> $HOME_BUILD/.android/repositories.cfg
fi

echo yyy | sdkmanager --update
ARRAY=$(echo $ANDROID_PACKAGES | tr ":" "\n")
for package in $ARRAY
do
  echo "install $package" && echo y | sdkmanager "$package"
done

echo "\e[33mINSTALL/BUILD ROS2 AMENT..."
mkdir -p $HOME_BUILD/ament_ws/src
cd $HOME_BUILD/ament_ws
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) -w $(pwd) $DOCKER_IMG sh -c "/usr/bin/wget https://gist.githubusercontent.com/Theosakamg/e6084cfafa6b7ea690104424cef970a2/raw/ament_java.repos -nv"
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) -w $(pwd) $DOCKER_IMG sh -c "/usr/bin/vcs import src < ament_java.repos"
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) -w $(pwd) $DOCKER_IMG sh -c "src/ament/ament_tools/scripts/ament.py build --symlink-install --isolated"

echo "\e[33mINSTALL ROS2 WS..."
mkdir -p $ROS2WS/src
cd $ROS2WS
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) -w $(pwd) $DOCKER_IMG sh -c "/usr/bin/wget https://gist.githubusercontent.com/Theosakamg/617cd893813163cdcb9943a08d667964/raw/ros2_java_android.repos -nv"
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) -w $(pwd) $DOCKER_IMG sh -c "/usr/bin/vcs import src < ros2_java_android.repos"

# Patch for Java support.
cd $ROS2WS/src/ros2/rosidl_typesupport && patch -p1 < ../../ros2_java/ros2_java/rosidl_ros2_android.diff

# Sync with git trigger
rm -rf $ROS2WS/src/ros2_java/ros2_android_examples && ln -s $HOME_BUILD/ros2java-alfred/ros2_android_examples $ROS2WS/src/ros2_java/ros2_android_examples

# Disable many package (not needed for android)
touch $ROS2WS/src/ros2/rosidl/python_cmake_module/AMENT_IGNORE
touch $ROS2WS/src/ros2/rosidl/rosidl_generator_py/AMENT_IGNORE

# TODO Check if needed ???
#cd $ROS2WS/src/eProsima/Fast-RTPS && git submodule init && git submodule update

# DEBUG
if [ $DEBUG -eq 1 ]
then
  echo "List $HOME_BUILD path"
  cd $HOME_BUILD
  find . -maxdepth 3 -type d -not \( -path "./.git" -prune \)
  echo "List $ROS2WS/src path"
  cd $ROS2WS/src
  find . -maxdepth 3 -type d -not \( -path "./.git" -prune \)
fi

echo "\e[33mBUILD ROS2 WS..."
cd $HOME_BUILD
docker run -u "$UID" -it --rm -v $(pwd):$(pwd) --env-file "$HOME_ENV" -w $(pwd) "$DOCKER_IMG" sh -c ". $HOME_BUILD/ament_ws/install_isolated/local_setup.sh && cd $ROS2WS && ament build --isolated --cmake-args -DPYTHON_EXECUTABLE=\"$PYTHON_PATH\" -DTHIRDPARTY=ON -DCMAKE_FIND_ROOT_PATH=\"$ROOT_PATH\" -DANDROID_FUNCTION_LEVEL_LINKING=OFF -DANDROID_TOOLCHAIN_NAME=\"$ANDROID_GCC\" -DANDROID_STL=gnustl_shared -DANDROID_ABI=\"$ANDROID_ABI\" -DANDROID_NDK=\"$ANDROID_NDK_HOME\" -DANDROID_NATIVE_API_LEVEL=\"$ANDROID_VER\" -DCMAKE_TOOLCHAIN_FILE=\"$ROS2JAVA_PATH\" -DANDROID_HOME=\"$ANDROID_SDK_ROOT\" -- --ament-gradle-args -Pament.android_stl=gnustl_shared -Pament.android_abi=\"$ANDROID_ABI\" -Pament.android_ndk=\"$ANDROID_NDK_HOME\" -- "

exit

