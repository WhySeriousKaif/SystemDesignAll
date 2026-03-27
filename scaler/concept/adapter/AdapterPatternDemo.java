package scaler.concept.adapter;

/**
 * Adapter Design Pattern Demonstration
 * Concept: Media Player (MP3 natively, MP4/VLC via Adapter)
 */

// 1. TARGET INTERFACE
interface MediaPlayer {
    void play(String audioType, String fileName);
}

// 2. ADVANCED INTERFACE (Adaptee Interface)
interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}

// 3. CONCRETE ADAPTEES
class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: " + fileName);
    }

    @Override
    public void playMp4(String fileName) { /* Do nothing */ }
}

class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) { /* Do nothing */ }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: " + fileName);
    }
}

// 4. THE ADAPTER (Bridges MediaPlayer to AdvancedMediaPlayer)
class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMusicPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}

// 5. CONCRETE TARGET (AudioPlayer)
class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;

    @Override
    public void play(String audioType, String fileName) {
        // Native support for mp3
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file natively. Name: " + fileName);
        }
        // Adapter handles other formats
        else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("❌ Invalid media: " + audioType + " format not supported");
        }
    }
}

// 6. MAIN EXECUTION
public class AdapterPatternDemo {

    public static void main(String[] args) {
        System.out.println("--- Adapter Design Pattern Demo ---\n");

        AudioPlayer player = new AudioPlayer();

        player.play("mp3", "beyond_the_horizon.mp3");
        player.play("mp4", "alone.mp4");
        player.play("vlc", "far_far_away.vlc");
        player.play("avi", "mind_me.avi");

        System.out.println("\n✅ Success: Legacy AudioPlayer now supports advanced formats via MediaAdapter.");
    }
}
