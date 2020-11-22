build-NaiveJavaRuntimeFunction:
	@ mvn clean package
	@ chmod +x ./target/classes/bootstrap
	@ cp -rf ./target/classes/bootstrap $(ARTIFACTS_DIR)
	@ jlink --module-path ./target/naive-java-runtime.jar:$(JAVA_HOME)/jmods \
         --add-modules  naive.java.runtime \
         --output ./target/dist \
         --launcher executable=naive.java.runtime/io.github.ajurasz.naivejavaruntime.Bootstrap \
         --compress 2 --no-header-files --no-man-pages --strip-debug
	@ cp -rf ./target/dist $(ARTIFACTS_DIR)