ReaBase {
    var <>name, <>chain, <>str, <>props;
}

ReaProj : ReaBase {

    *new {
        arg properties;
        ^super.new.init(properties);
    }

    init {
        arg properties;
        props = properties;
        chain = List.new();
        name = "REAPER_PROJECT";
        str = String.new();
    }

    getChain {
        chain.postln;
    }

    getStr {
        ^str
    }

    traverse {
        arg origin;

        this.str = this.str ++ format("<%\n", origin.name);

        if (origin.props.notNil,
            {
                origin.props.pairsDo({
                    arg key, value;
                    this.str = this.str ++ format("% %\n", key.toUpper, value)
                })
            }
        );

        if (origin.chain.notNil,
            {
                origin.chain.do({ |node|
                    this.traverse(node);
                });
            },
            {};
        );

        this.str = this.str ++ format(">\n");
    }

    addTrack {
        arg track;
        chain.add(track);
        ^this;
    }

    write {
        arg path;
        var outPath, outFile;
        outPath = PathName.new(path);
        this.traverse(this);
        // File.mkdir()
        // File.exists()
        outFile = File.new(outPath.absolutePath, "w");
        outFile.write(this.str);
        outFile.close;
    }
}

ReaTrack : ReaBase {

    *new {
        arg properties;
        ^super.new.init(properties);
    }

    init {
        arg properties;
        props = properties;
        chain = List.new();
        name = "TRACK";
    }

    addItem {
        arg item;
        chain.add(item);
        ^this;
    }

}

ReaItem : ReaBase {
    var psource, pstart, plength;

    *new {
        arg source, start, length, properties;
        ^super.new.init(source, start, length, properties);
    }

    init {
        arg source, start, length, properties;
        props = properties;
        psource = PathName.new(source);
        pstart = start;
        plength = length;
        name = format("ITEM\nPOSITION %\nLENGTH %\n <SOURCE WAVE\n FILE '%'\n>",
            start,
            length,
            psource.absolutePath
        )
    }
}
