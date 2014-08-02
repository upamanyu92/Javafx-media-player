package newapplication;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author SMART
 */
public class FXMLDocumentController implements Initializable, ControlledScreen {

    ScreenControl myController;
    @FXML
    Slider test = new Slider();
    @FXML
    Hyperlink rpt = new Hyperlink();
    @FXML
    Label songlabel = new Label();
    @FXML
    Button FWD = new Button();
    @FXML
    Button BWD = new Button();
    @FXML
    Button play = new Button();
    @FXML
    Button Pause = new Button();
    @FXML
    VBox vbox = new VBox();
    @FXML
    Slider vol = new Slider();
    @FXML
    Slider slider = new Slider();
    @FXML
    Button stop = new Button();
    @FXML
    private final ListView<File> myListView = new ListView<>();
    @FXML
    public ImageView albumCover;
    @FXML
    Label album = new Label();
    @FXML
    Label artist = new Label();
    @FXML
    Label title = new Label();
    @FXML
    Label year = new Label();
    @FXML
    Hyperlink mute = new Hyperlink();
    @FXML
    Label ascTime = new Label();
    @FXML
    Label dscTime = new Label();
    @FXML
    Label volbl = new Label();
    @FXML
    Label tf = new Label();
    @FXML
    Label pitch = new Label();
    @FXML
    HBox hb2 = new HBox(40);
    @FXML
    HBox hb1 = new HBox(40);
    @FXML
    ToggleButton eqlzr = new ToggleButton("EQLZR");
    @FXML
    Slider eq1 = new Slider();
    @FXML
    Slider eq2 = new Slider();
    @FXML
    Slider eq3 = new Slider();
    @FXML
    Slider eq4 = new Slider();
    @FXML
    Slider eq5 = new Slider();
    @FXML
    Slider eq6 = new Slider();
    public MediaPlayer player;
    Media mediaFile;
    File file;
    MediaView mediaview = new MediaView();
    ObservableList<File> list = FXCollections.observableArrayList();
    List<File> fileList;
    String mtitle;
    int flag;
    HBox hbox = new HBox(2);
    public static final double START_FREQ = 250.0;
    public static final int BAND_COUNT = 7;

    public void playList() {
        FileChooser fileChooser = new FileChooser();
        //Open directory from existing directory
        try {
            if (fileList != null) {
                if (!fileList.isEmpty()) {
                    File existDirectory = fileList.get(0).getParentFile();
                    fileChooser.setInitialDirectory(existDirectory);
                }
            }
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Supported Music files", "*.mp3");
            fileChooser.getExtensionFilters().add(extFilter);
            //Show open file dialog, with primaryStage blocked.
            fileList = fileChooser.showOpenMultipleDialog(null);
            //list.clear();




            for (int i = 0; i < fileList.size(); i++) {
                list.add(fileList.get(i));
            }

            myListView.setItems(list);
            myListView.setOpacity(0.80);
            myListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    System.out.println("Clicked in List");
                    selectFromList();
                    

                }
            });
            
        } catch (Exception e) {
            e.getMessage();
        }









    }

    /**
     * clearList() method used for clearing Song List
     */
    public void clearList() {
        list.clear();
    }

    public void deleteItem() {
        int del = myListView.getSelectionModel().getSelectedIndex();
        list.remove(del);
    }

    public void listDir() {
        //final DirectoryChooser fileChooser = new DirectoryChooser();

        final DirectoryChooser dirChooser = new DirectoryChooser();

        final File selectedDirectory = dirChooser.showDialog(null);
        if (selectedDirectory != null) {
            selectedDirectory.getAbsolutePath();
            if (selectedDirectory.isDirectory()) {
                File[] loopsList = selectedDirectory.listFiles();
                for (File f : loopsList) {
                    String s = f.getName();
                    if (s.endsWith(".mp3")) {
                        list.add(f);


                    }
                    myListView.setItems(list);
                    myListView.setOpacity(0.80);
                    myListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent me) {
                            System.out.println("Clicked in List");
                            selectFromList();

                        }
                    });
                }
            }
        }



    }

    public void selectFromList() {
        File selectedItem = myListView.getSelectionModel().getSelectedItem();
        mediaPlay(selectedItem);

    }

    public void mediaPlay(File url) {

        //System.out.println(mediafile);

        mediaFile = new Media(url.toURI().toString());


        player = new MediaPlayer(mediaFile);
        createEQInterface();
        player.getAudioEqualizer().setEnabled(false);


        //mediaview.setMediaPlayer(player);

        mediaFile.getMetadata().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> ch) {
                if (ch.wasAdded()) {
                    handleMetadata(ch.getKey(), ch.getValueAdded());
                }
            }
        });



        Pause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                player.pause();
                //vbox.getChildren().remove(hbox);
                System.out.println(player.getStatus());
                Pause.setDisable(true);
                play.setDisable(false);


            }
        });


        play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                player.setVolume(vol.getValue());
                //vbox.getChildren().remove(hbox);
                player.play();
                play.setDisable(true);
                Pause.setDisable(false);
                stop.setDisable(false);


            }
        });
        stop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                player.stop();
                //vbox.getChildren().remove(hbox);
                play.setDisable(false);
                stop.setDisable(true);
                Pause.setDisable(true);

            }
        });
        BWD.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {

                try {
                    player.stop();
                    flag = 1;
                    vbox.getChildren().remove(hbox);
                    myListView.getSelectionModel().selectPrevious();
                    File selectedItem = myListView.getSelectionModel().getSelectedItem();
                    mediaPlay(selectedItem);
                    player.play();
                    player.setVolume(vol.getValue());


                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
       
        FWD.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {

                try {

                    player.stop();
                    flag = 1;
                    vbox.getChildren().removeAll(hbox);
                    myListView.getSelectionModel().selectNext();
                    File selectedItem = myListView.getSelectionModel().getSelectedItem();
                    
                    mediaPlay(selectedItem);
                    vbox.getChildren().removeAll(hbox);
                    player.play();
                    player.setVolume(vol.getValue());

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {

                if (player.getCycleCount() != MediaPlayer.INDEFINITE) {
                    // vbox.getChildren().remove(hbox);
                    player.stop();
                    vbox.getChildren().remove(hbox);
                    System.out.println(myListView.getSelectionModel().getSelectedIndex());
                    myListView.getSelectionModel().selectNext();
                    File selectedItem = myListView.getSelectionModel().getSelectedItem();

                    mediaPlay(selectedItem);

                    player.play();
                    player.setVolume(vol.getValue());
                }
            }
        });
        player.setOnPaused(new Runnable() {
            @Override
            public void run() {
                tf.setText("Paused");

            }
        });
        player.setOnStopped(new Runnable() {
            @Override
            public void run() {
                tf.setText("Stopped");
            }
        });
        player.setOnError(new Runnable() {
            @Override
            public void run() {
                System.out.println("Error found While loading : " + player.getError());
            }
        });

        rpt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (Color.BLACK == rpt.getTextFill()) {
                    player.setCycleCount(MediaPlayer.INDEFINITE);
                    rpt.setTextFill(Color.WHITE);
                } else {
                    player.setCycleCount(0);
                    rpt.setTextFill(Color.BLACK);
                }
            }
        });

        player.setOnPlaying(new Runnable() {
            @Override
            public void run() {


                tf.setText("Playing");
                songlabel.setText(mtitle);


            }
        });

        player.setOnReady(new Runnable() {
            @Override
            public void run() {


                tf.setText("Ready");

                /* if (player != null) {
                 vbox.getChildren().remove(hbox);
                 }
                 if (player.getCurrentTime() == player.getStopTime()) {
                 vbox.getChildren().remove(hbox);
                 }
                 if (flag == 1) {
                 vbox.getChildren().remove(hbox);
                 flag = 0;
                 }*/
                try{
                vbox.getChildren().remove(hbox);
                }
                catch(Exception e){}
                final int bands = player.getAudioSpectrumNumBands();
                final Rectangle[] rects = new Rectangle[bands / 2];

                final Random rand = new Random(System.currentTimeMillis());
                for (int i = 0; i < rects.length - 15; i++) {
                    int red = rand.nextInt(255);
                    int green = rand.nextInt(255);
                    int blue = rand.nextInt(255);

                    rects[i] = new Rectangle();
                    rects[i].setFill(Color.rgb(red, green, blue));
                    hbox.getChildren().add(rects[i]);
                    hbox.setAlignment(Pos.BOTTOM_LEFT);
                }
                vbox.getChildren().add(hbox);


                try {
                    double w = vbox.getWidth();
                    hbox.setMinWidth(w);
                    int bandWidth = (int) (w / rects.length);
                    for (Rectangle r : rects) {
                        r.setWidth(bandWidth);
                        r.setHeight(1);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }


                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());
                System.out.println(player.getTotalDuration().toSeconds());
                player.setAudioSpectrumListener(new AudioSpectrumListener() {
                    @Override
                    public void spectrumDataUpdate(double v, double vl, float[] mags, float[] floatsl) {

                        for (int i = 0; i < rects.length - 15; i++) {
                            double h = mags[i] + 60;
                            for (int band = 0; band < 255; band++) {
                                songlabel.setTextFill(Color.rgb(band, band, band));
                            }
                            if (h > 1) {
                                rects[i].setHeight(h);
                            }
                        }
                    }
                });



            }
        });

        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration currnt) {
                slider.setValue(currnt.toSeconds());
                ascTime.setText(formatDuration(player.getCurrentTime()));
                dscTime.setText(formatDuration(player.getBufferProgressTime()));

                //System.out.println(currnt.toSeconds());
            }
        });

        pitch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                test.setValue(1.0);
                player.setRate(1.0);

            }
        });

        test.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                player.setRate(test.getValue());

            }
        });
        slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });
        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });

    }

    public String formatDuration(Duration duration) {
        double millis = duration.toMillis();
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) (millis / (1000 * 60));
        return String.format("%02d : %02d", minutes, seconds);
    }

    //For Equalizer setup
    @FXML
    public void createEQInterface() {
        try{
        final MediaPlayer mp = player;
        createEQBands(mp);
        }
        catch(Exception e){
        System.out.println(e.getMessage());
        }
    }
//creating bands for audio equalization

    public void createEQBands(MediaPlayer mp) {
        final ObservableList<EqualizerBand> bands = mp.getAudioEqualizer().getBands();
        bands.clear();
        double min = EqualizerBand.MIN_GAIN;
        double max = EqualizerBand.MAX_GAIN;
        double mid = (max - min) / 2;
        double freq = START_FREQ;
// Create the equalizer bands with the gains preset to
// a nice cosine wave pattern.
        for (int j = 0; j < BAND_COUNT; j++) {
// Use j and BAND_COUNT to calculate a value between 0 and 2*pi
            double theta = (double) j / (double) (BAND_COUNT - 1) * (2 * Math.PI);
// The cos function calculates a scale value between 0 and 0.4
            double scale = 0.4 * (1 + Math.cos(theta));
// Set the gain to be a value between the midpoint and 0.9*max.
            double gain = min + mid + (mid * scale);
            bands.add(new EqualizerBand(freq, freq / 2, gain));
            freq *= 2;
        }

        EqualizerBand eb = bands.get(1);
        eq1.setMax(max);
        eq1.setMin(min);
        eq1.valueProperty().bindBidirectional(eb.gainProperty());
        final Label l1 = new Label(formatFrequency(eb.getCenterFrequency()));
        l1.setTextFill(Color.GRAY);


        EqualizerBand eb2 = bands.get(2);
        eq2.setMax(max);
        eq2.setMin(min);
        eq2.valueProperty().bindBidirectional(eb2.gainProperty());
        final Label l2 = new Label(formatFrequency(eb2.getCenterFrequency()));
        l2.setTextFill(Color.GRAY);
        EqualizerBand eb3 = bands.get(3);
        eq3.setMax(max);
        eq3.setMin(min);
        eq3.valueProperty().bindBidirectional(eb3.gainProperty());
        final Label l3 = new Label(formatFrequency(eb3.getCenterFrequency()));
        l3.setTextFill(Color.GRAY);
        
        EqualizerBand eb4 = bands.get(4);
        eq4.setMax(max);
        eq4.setMin(min);
        eq4.valueProperty().bindBidirectional(eb4.gainProperty());
        final Label l4 = new Label(formatFrequency(eb4.getCenterFrequency()));
        l4.setTextFill(Color.GRAY);
        
        
        EqualizerBand eb5 = bands.get(5);
        eq5.setMax(max);
        eq5.setMin(min);
        eq5.valueProperty().bindBidirectional(eb5.gainProperty());
        final Label l5 = new Label(formatFrequency(eb5.getCenterFrequency()));
        l5.setTextFill(Color.GRAY);
        
        
        EqualizerBand eb6 = bands.get(6);
        eq6.setMax(max);
        eq6.setMin(min);
        eq6.valueProperty().bindBidirectional(eb6.gainProperty());
        final Label l6 = new Label(formatFrequency(eb6.getCenterFrequency()));
        l6.setTextFill(Color.GRAY);
        hb1.getChildren().addAll(l1, l2, l3, l4, l5, l6);

    }

    public String formatFrequency(double centerFrequency) {
        if (centerFrequency < 1000) {
            return String.format("%.0f Hz", centerFrequency);
        } else {
            return String.format("%.1f kHz", centerFrequency / 1000);
        }
    }

    public void handleMetadata(String key, Object value) {
        if (key.equals("album")) {
            System.out.println(value);
            album.setText("album  : " + value.toString());
            if ("".equals(value.toString()) || value.toString() == null) {
                album.setText("<Unknown>");
            }
        }

        if (key.equals("artist")) {
            System.out.println(value);
            artist.setText("artist    : " + value.toString());
            if ("".equals(value.toString()) || value.toString() == null) {
                artist.setText("<Unknown>");
            }
        }
        if (key.equals("title")) {
            System.out.println(value);
            title.setText("title      : " + value.toString());
            mtitle = value.toString();
            if ("".equals(value.toString()) || value.toString() == null) {
                title.setText("<Unknown>");
            }

        }
        if (key.equals("year")) {
            System.out.println(value);
            year.setText("year     : " + value.toString());
            if ("".equals(value.toString()) || value.toString() == null) {
                year.setText("<Unknown>");
            }
        }
        if (key.equals("image")) {
            albumCover.setImage((Image) value);
            if (value == null) {
                albumCover.setImage(null);

            }
        }
    }

    public void close() {
        Platform.exit();
    }

    @Override
    public void setScreenParent(ScreenControl screenParent) {
        myController = screenParent;
    }

    @FXML
    private void goToScreen2(ActionEvent event) {
        myController.setScreen(NewApplication.screen2ID);
    }

    @FXML
    private void goToScreen3(ActionEvent e) {
        myController.setScreen(NewApplication.screen3ID);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 



        eqlzr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (eqlzr.selectedProperty().getValue()) {
                    hb2.setVisible(true);
                    hb1.setVisible(true);
                    player.getAudioEqualizer().setEnabled(true);

                } else {
                    hb2.setVisible(false);
                    hb1.setVisible(false);
                    player.getAudioEqualizer().setEnabled(false);

                }

            }
        });
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (player.isMute()) {
                    mute.setTextFill(Color.BLACK);
                    player.setMute(false);
                } else {
                    player.setMute(true);
                    mute.setTextFill(Color.WHITE);
                }
            }
        });

        vol.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                try {
                    vol.valueProperty().bindBidirectional(player.volumeProperty());

                    double s = vol.getValue() * 100;
                    int a = (int) s;
                    volbl.setText("Volume[" + a + "%]");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


    }
}
