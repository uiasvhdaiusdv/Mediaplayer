package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller implements Initializable{
	
	URL resource;

    Media media;

    MediaPlayer mediaPlayer;
    
    @FXML
    Slider volumeSlider;
    
    @FXML
    private BorderPane borderpane;
    
    private double volume = 0.03;
    
    Mp3File mp3file;
    
    @FXML
    TextField titelField;
    
    @FXML
    TextField titelNumberField;
    
    @FXML
    TextField albumField;
    
    @FXML
    TextField artistField;
    
    @FXML
    TextField yearField;
    
    @FXML
    TextField commentField;
    
    @FXML
    TextField composerField;
    
    @FXML
    Label fileName;
    
    public void handlePlayButtonActionOnClick() {
    	if (mediaPlayer != null)
    		mediaPlayer.play();
	}
	
	public void handlePauseButtonActionOnClick() {
		if (mediaPlayer != null)
    		mediaPlayer.pause();
	}
	
	public void handleStopButtonActionOnClick() {
		if (mediaPlayer != null)
    		mediaPlayer.stop();
	}
	
	public void singleFileChooser (ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("MP3 Files", "*.mp3"));
		File f = fc.showOpenDialog(null);
		
		if (f != null) {
			if (mediaPlayer != null)
				mediaPlayer.stop();
			this.singleFileChooserMP3Tags(f);
			media = new Media(f.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(volumeSlider.getValue() / 100);
			fileName.setText(f.getName());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		volumeSlider.setValue(volume * 100);
		
		volumeSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				if (mediaPlayer != null) 
					mediaPlayer.setVolume(volumeSlider.getValue() / 100);
			}
			
		});
		
	}
	
	public void saveMp3File() {
		
		if (this.mp3file == null)
			System.out.println("mp3 is null (saveMp3File)");
		else
			saveWithLocation(this.mp3file);
	}
	
	public void saveWithLocation(Mp3File mp3fileToSave) {
		if (mp3fileToSave == null) {
			System.out.print("MP3File is Null");
			return;
		}
		
		ID3v2 id3v2Tagnew;
    	id3v2Tagnew = mp3fileToSave.getId3v2Tag();
    	id3v2Tagnew.setTitle(this.titelField.getText());
    	id3v2Tagnew.setTrack(this.titelNumberField.getText());
    	id3v2Tagnew.setAlbum(this.albumField.getText());
    	id3v2Tagnew.setArtist(this.artistField.getText());
    	id3v2Tagnew.setYear(this.yearField.getText());
    	id3v2Tagnew.setComment(this.commentField.getText());
    	id3v2Tagnew.setComposer(this.composerField.getText());
    	
    	try {
    		FileChooser fc = new FileChooser();
    		fc.getExtensionFilters().add(new ExtensionFilter("MP3 Files", "*.mp3"));
    		File f = fc.showSaveDialog(null);
    		String filePath = f.getAbsolutePath();
			//mp3fileToSave.save("C:\\Users\\Christopher\\Desktop\\Music\\test123.mp3");
    		mp3fileToSave.save(filePath);
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void singleFileChooserMP3Tags(File f) {
		if(f == null) return;
		try {
			this.mp3file = new Mp3File(f);
			
			if (mp3file.hasId3v2Tag()) {
	        	ID3v2 id3v2Tag = mp3file.getId3v2Tag();
	        	String songtitel = id3v2Tag.getTitle();
	        	if (songtitel == null) System.out.println("songtitel = null");
	        	else if (this.titelField == null) System.out.println("titelField = null");
	        	this.titelNumberField.setText(id3v2Tag.getTrack());
	        	this.titelField.setText(id3v2Tag.getTitle());
	        	this.albumField.setText(id3v2Tag.getAlbum());
	        	this.artistField.setText(id3v2Tag.getArtist());
	        	this.yearField.setText(id3v2Tag.getYear());
	        	this.commentField.setText(id3v2Tag.getComment());
	        	this.composerField.setText(id3v2Tag.getComposer());
	        	
		    }
			
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
