ReaperBatch : ReaperCMD {

    *prFilenameArrayToString{|fileNameArray|
        // Check
        var string;

        fileNameArray.notNil.if({
            string = if(
                fileNameArray.size > 0 &&
                fileNameArray.isArray &&
                fileNameArray.every{|fileNamePair|
                    var fileName = fileNamePair[0].standardizePath;
                    var res = File.exists(fileName);
                    if(res == false, {
                        "%: File % does not exist".format(this.name, fileName).error
                    });

                    res
                },
                fileNameArray.every{|fileNamePair|
                    (fileNamePair.size == 2) &&
                    (fileNamePair.every{|fn| fn.isKindOf(String)})
                },
                {
                    // Convert
                    var fnString = "";
                    fileNameArray.do{|fnpair|
                        fnString = fnString ++ fnpair[0].standardizePath ++ "\t" ++ fnpair[1].standardizePath ++ "\n"
                    };

                    fnString;

                }, {
                    "%: fileNameArray is incorrect. Must be an array of arrays where each sub array is size 2 and contains two strings".format(this.name).error;
                    nil
                }
            );
        }, {

            "%: fileNameArray is nil. Must be an array of arrays where each sub array is size 2 and contains two strings".format(this.name).error;
        });

        // Return
        ^string

    }

    *convert{|
        fileNameArray, // Array of file name strings ala: [["infile1.wav", "outfile1.wav"], ["infile2.wav", "outfile2.wav"]]
        fxchainfilename, // (use full path if specified, otherwise FxChains directory)
        outpath,
        sampleRate=nil,  // Use source if nil
        numChans=nil, // Use source if nil
        dither=3, //(1=dither, 2=noise shaping, 3=both)
        usesrcstart=1, //(1=write source media BWF start offset to output)
        usesrcmetadata=1, //(1=attempt to preserve original media file metadata if possible)
        padstart=1, //(leading silence in sec, can be negative)
        padend=1, // (trailing silence in sec, can be negative)
        outpattern=nil,
        normalize=1, //(1=peak, 2=true peak, 3=lufs-i, 4=lufs-s, 5=lufs-m,
        normalizedb=(-6.0),
        normalizeonlyiftooloud=0, //1=normalize only if too loud)
        brickwall=1, //(1=peak, 2=true peak, 2nd parameter is dB)
        brickwalldb=1.0,
        fxnumchans=nil // (if not specified, FX will be configured to 4 channels)
        |

        var files = this.prFilenameArrayToString(fileNameArray);
        var configBlock = "";
        if(files.notNil && fxchainfilename.notNil && outpath.notNil, {
            var outFile;

            configBlock = configBlock ++ "<CONFIG \n";
            if(sampleRate.notNil, {
                configBlock = configBlock ++ "SRATE %\n".format(sampleRate);
            });

            if(numChans.notNil, {
                configBlock = configBlock ++ "NCH %\n".format(numChans);
            });

            // configBlock = configBlock ++ "RSMODE modeidx (resample mode, copy from project file)\n");
            configBlock = configBlock ++ "DITHER % \n".format(dither);
            configBlock = configBlock ++ "USESRCSTART % \n".format(usesrcstart);
            configBlock = configBlock ++ "USESRCMETADATA % \n".format(usesrcmetadata);
            configBlock = configBlock ++ "PAD_START % \n".format(padstart);
            configBlock = configBlock ++ "PAD_END % \n".format(padend);
            configBlock = configBlock ++ "OUTPATH '%'\n".format(outpath.standardizePath);
            if(outpattern.notNil, {
                configBlock = configBlock ++ "OUTPATTERN '%'\n".format(outpattern);
            });
            configBlock = configBlock ++ "NORMALIZE % % % \n".format(normalize, normalizedb, normalizeonlyiftooloud);
            configBlock = configBlock ++ "BRICKWALL % % \n".format(brickwall, brickwalldb);
            configBlock = configBlock ++ "FXCHAIN '%' \n".format(fxchainfilename);
            if(fxnumchans.notNil,{
                configBlock = configBlock ++ "FX_NCH % \n".format(fxnumchans);
            });

            // TODO: Wat te fuck is this:
            // configBlock = configBlock ++ "<FXCHAIN\n";
            // configBlock = configBlock ++ "  (contents of .RfxChain file)\n");
            // configBlock = configBlock ++ ">\n");

            // TODO: Wat te fuck is this:
            // configBlock = configBlock ++ "<O\n"UTFMT;
            // configBlock = configBlock ++ "  (base64 data, e.g. contents of <RENDER_CFG or <RECORD_CFG block from project file)\n");
            // >

            // TODO: Wat te fuck is this:
            // <METADATA
            //   (contents of <RENDER_METADATA block from project file)
            // >

            configBlock = configBlock ++ ">\n";

            configBlock = files ++ configBlock;

            outFile = this.prMakeFileListTXT(configBlock);

            this.runCommand("-nosplash -batchconvert %".format(outFile));

            "Log output:".postln;
            File.readAllString((outFile ++ ".log").postln).postln

        }, {
            "%: A mandatory argument was not set".format(this.name).error
        });
    }

    *prMakeFileListTXT{|configString|
        var outFile = PathName.tmp +/+ "filelist.txt";

        "%: Creating % file containing:\n%".format(this.name, outFile, configString).postln;

        File
        .open(outFile, mode:"w")
        .write(configString)
        .close();

        ^outFile

    }
}
