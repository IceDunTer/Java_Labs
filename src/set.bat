set PATH_TO_FX="C:\javafx-26_windows-x64_bin-sdk\javafx-sdk-26\lib"

javac --module-path %PATH_TO_FX% -encoding UTF-8 -d out gui/WindowApp.java core/DataProcessor.java

java --module-path %PATH_TO_FX% -cp out gui.WindowApp