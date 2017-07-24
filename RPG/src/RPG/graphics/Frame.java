package RPG.graphics;


public class Frame {

    private TextureRegion frame;
    private int duration;

    public Frame(TextureRegion frame, int duration) {
        this.frame = frame;
        this.duration = duration;
    }

    public TextureRegion getFrame() {
        return frame;
    }

    public void setFrame(TextureRegion frame) {
        this.frame = frame;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}