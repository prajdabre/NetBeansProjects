


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class musicplayer {
String bip = "bip.mp3";
Media hit = new Media(bip);
MediaPlayer mediaPlayer = new MediaPlayer(hit);
mediaPlayer.play();
}
