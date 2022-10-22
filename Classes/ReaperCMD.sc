ReaperCMD{
    classvar <>executableName;

    *initClass{

        // Set default for executable, if it isn't set already
        if(executableName.isNil, {

            Platform.case(
                \osx,       {
                    executableName = "/Applications/REAPER.app/Contents/MacOS/REAPER"
                },
                \linux,     {
                    executableName ="reaper"
                },
                \windows,   {
                    // TODO
                }
            );

        });
    }

    *prIsReaperExecutable{
        var checkCmd = "command -v ".format(this.executableName());
        ^checkCmd.systemCmd == 0
    }

    *runCommand{|argumentString|
        var cmd = this.executableName() ++ " " ++ argumentString;
        "Running Reaper command: %".format(cmd).postln;
        if(this.prIsReaperExecutable(), {
            ^cmd.systemCmd()
        }, {
            "%: reaper binary % could not be found".format(this.name, this.executableName()).error;
        })

    }

}
