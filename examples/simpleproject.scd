/*

This example creates a Reaper Project, adds 100 tracks to it and places an item on each of those tracks

*/
(
var projectPath = "~/tmp/reacollider-%.RPP".format(Date.getDate.stamp).asAbsolutePath;
var project = ReaProj.new;
var numTracks = 100;
var tracks = numTracks.collect{|i|
	var props = ();

	// Track name
	props.put(\NAME, "supercollider_track_%".format(i));

	/*
	// Other kinds of properties for tracks:

	PEAKCOL 16576
    BEAT -1
    AUTOMODE 0
    VOLPAN 1 0 -1 -1 1
    MUTESOLO 0 0 0
    IPHASE 0
    PLAYOFFS 0 1
    ISBUS 0 0
    BUSCOMP 0 0 0 0 0
    SHOWINMIX 1 0.6667 0.5 1 0.5 -1 -1 -1
    FREEMODE 0
    SEL 0
    REC 0 0 1 0 0 0 0
    TRACKHEIGHT 0 0 0 0 0 0
    INQ 0 0 0 0.5 100 0 0 100
    NCHAN 2
    FX 1
    PERF 0
    MIDIOUT -1
    MAINSEND 1 0
	*/

	ReaTrack.new(props)
};

// A sound source
var aSoundSource = Platform.resourceDir +/+ "sounds" +/+ "a11wlk01.wav";
var sourceInfo = SoundFile.openRead(aSoundSource).close;

/*
// Potential item properties:

POSITION 0
SNAPOFFS 0
LENGTH 4.2832879818594
LOOP 1
ALLTAKES 0
FADEIN 1 0 0 1 0 0 0
FADEOUT 1 0 0 1 0 0 0
MUTE 0 0
SEL 0
IGUID {4CCE73D7-6918-540D-3055-F49A86303856}
IID 2
NAME ""
VOLPAN 1 0 1 -1
SOFFS 0
PLAYRATE 1 1 0 -1 0 0.0025
CHANMODE 0

*/
var itemProperties = (NAME: PathName(aSoundSource).fileNameWithoutExtension);
var itemPlacement = 0;

// Create an item from the sound source
var anItem = ReaItem.new(
	source:aSoundSource,
	start:itemPlacement,
	length:sourceInfo.duration,
	properties:itemProperties
);

// Add tracks
tracks.do{|track|
	track.addItem(anItem);
	project.addTrack(track)
};

// Create the project
project.write(projectPath);

// projectPath.openOS;

)
