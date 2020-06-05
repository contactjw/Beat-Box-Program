import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;


public class BeatBox {

    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList;                           //Where we store the checkboxes
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;

    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal",
            "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom",
            "High Agogo", "Open Hi Conga"};
    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    public static void main(String[] args) {
        new BeatBox().buildGui();
    }

    public void buildGui() {
        theFrame = new JFrame("Cyber Beatbox");             //Give the window a title
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit program on close
        BorderLayout layout = new BorderLayout();                //Create a border layout
        JPanel background = new JPanel(layout);                  //Set the panel layout style to borderlayout
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add spacing

        checkboxList = new ArrayList<JCheckBox>();               //Where we store the checkboxes
        Box buttonBox = new Box(BoxLayout.Y_AXIS);               //Set the buttons in a vertical layout

        JButton start = new JButton("Start");               //Create Start button
        start.addActionListener(new MyStartListener());          //Give it an actionListener
        buttonBox.add(start);                                    //Add it to the buttonBox

        JButton stop = new JButton("Stop");                 //Create Stop button
        stop.addActionListener(new MyStopListener());            //Give it an actionListener
        buttonBox.add(stop);                                     //Add it to the buttonBox

        JButton upTempo = new JButton("Tempo Up");          //Create Tempo Up button
        upTempo.addActionListener(new MyUpTempoListener());      //Give it an actionListener
        buttonBox.add(upTempo);                                  //Add it to the buttonBox

        JButton downTempo = new JButton("Tempo Down");      //Create Tempo Down button
        downTempo.addActionListener(new MyDownTempoListener());  //Give it an actionListener
        buttonBox.add(downTempo);                                //Add it to the buttonBox

        JButton clear = new JButton("Clear");
        clear.addActionListener(new MyClearListener());
        buttonBox.add(clear);

        Box nameBox = new Box(BoxLayout.Y_AXIS);                 //Create a box to store the instrument name labels
        for (int i = 0; i < 16; i++) {                           //Loop 16 times for each instrument label
            nameBox.add(new Label(instrumentNames[i]));          //Add each label to the nameBox starting from 0 index
        }

        background.add(BorderLayout.EAST, buttonBox);            //Add the buttonBox to the East Border
        background.add(BorderLayout.WEST, nameBox);              //Add the nameBox to the West Border

        theFrame.getContentPane().add(background);               //Add the borderLayout panel to the frame.

        GridLayout grid = new GridLayout(16, 16);     //Create a new gridlayout we will later add to
        //the background panel for the buttons.
        grid.setVgap(1);                                         //Set vertical gap between buttons to 1 space
        grid.setHgap(2);                                         //Set horizontal gap between buttons to 2 spaces
        mainPanel = new JPanel(grid);                            //Create the panel object and give it the grid layout
        background.add(BorderLayout.CENTER, mainPanel);          //Add the mainPanel to background at the center

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();                       //Create checkbox object
            c.setSelected(false);                                //Make it so the checkboxes are not checked
            checkboxList.add(c);                                 //Add it to the arrayList
            mainPanel.add(c);                                    //Add it to the gui in mainPanel gridlayout
        }

        setUpMidi();                                             //Create the basic sequencer objects

        theFrame.setBounds(50, 50, 300, 300);//Set the size of the main window
        theFrame.pack();                                         //The center will get its size and the rest will work around it
        theFrame.setVisible(true);                               //Make the window visible to user
    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {e.printStackTrace();}
    }

    public void buildTrackAndStart() {
        int[] trackList = null;

        sequence.deleteTrack(track);                            //Get rid of the old track
        track = sequence.createTrack();                         //Create a new track

        for (int i = 0; i < 16; i++) {
            trackList = new int[16];                            //Create a 16x16 array
            int key = instruments[i];                           //Set the key for this iteration to instrument

            for (int j = 0; j < 16; j++) {
                JCheckBox jc = checkboxList.get(j + 16*i);      //Get the state of the checkboxes
                if (jc.isSelected()) {
                    trackList[j] = key;                         //If the user selected it, set tracklist to that instrument
                } else {
                    trackList[j] = 0;                           //If not selected, set to 0
                }
            }
            makeTracks(trackList);
            track.add(makeEvent(176, 1, 127, 0, 16));
        }

        track.add(makeEvent(192, 9, 1, 0, 15));
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);//Play the beats endlessly
            sequencer.start();                                  //Start playing
            sequencer.setTempoInBPM(120);                       //Set Beat Tempo
        } catch (Exception e) {e.printStackTrace();}
    }

    //inner classes
    public class MyStartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            buildTrackAndStart();
        }
    }

    public class MyStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            sequencer.stop();
        }
    }

    public class MyUpTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * 1.03));
        }
    }

    public class MyDownTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * .97));
        }
    }

    public class MyClearListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {

            sequence.deleteTrack(track);                            //Get rid of the old track
            track = sequence.createTrack();                         //Create a new track

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    JCheckBox jc = checkboxList.get(j + 16*i);      //Get the state of the checkboxes
                    if (jc.isSelected()) {
                        jc.setSelected(false);                         //If the user selected it, set tracklist to that instrument
                    }
                }
            }
        }
    }

    public void makeTracks(int[] list) {
        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(144, 9, key, 100, i));
                track.add(makeEvent(128, 9, key, 100, i + 1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) {e.printStackTrace();}
        return event;
    }



}
