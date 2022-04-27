ReaperCMD{
    // TODO: Include custom prefixes
    *executableName{
        ^"reaper"
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
