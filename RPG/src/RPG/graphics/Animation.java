package RPG.graphics;

import java.util.ArrayList;
import java.util.List;

import RPG.graphics.Frame;
import RPG.graphics.TextureRegion;

public class Animation
{
	public int frameCount; // Counts ticks for change
	public int frameDelay; // frame delay 1-12 (You will have to play around with this)
	public int currentFrame; // animations current frame
	public int animationDirection; // animation direction (i.e counting forward or backward)
	public int totalFrames; // total amount of frames for your animation

	public boolean stopped; // has animations stopped

	public List<Frame> frames = new ArrayList<Frame>(); // Arraylist of frame

	public Animation(TextureRegion frames[], int frameDelay)
	{
		this.frameDelay = frameDelay;
		this.stopped = false;

		for (int i = 0; i < frames.length; i++)
		{
			addFrame(frames[i], frameDelay);
		}

		this.frameCount = 0;
		this.frameDelay = frameDelay;
		this.currentFrame = 0;
		this.animationDirection = 1;
		this.totalFrames = this.frames.size();

	}

	private void addFrame(TextureRegion frame, int duration)
	{
		if (duration <= 0)
		{
			System.err.println("Invalid duration: " + duration);
			throw new RuntimeException("Invalid duration: " + duration);
		}
		frames.add(new Frame(frame, duration));
		currentFrame = 0;
	}

	public TextureRegion getSprite()
	{
		return frames.get(currentFrame).getFrame();
	}

	public void update()
	{
		if (!stopped)
		{
			frameCount++;

			if (frameCount > frameDelay)
			{
				frameCount = 0;
				currentFrame += animationDirection;

				if (currentFrame > totalFrames - 1)
				{
					currentFrame = 0;
				}
				else if (currentFrame < 0)
				{
					currentFrame = totalFrames - 1;
				}
			}
		}

	}

	public void start()
	{
		if (!stopped)
		{
			return;
		}

		if (frames.size() == 0)
		{
			return;
		}

		stopped = false;
	}

	public void stop()
	{
		if (frames.size() == 0)
		{
			return;
		}

		stopped = true;
	}

	public void restart()
	{
		if (frames.size() == 0)
		{
			return;
		}
		stopped = false;
		currentFrame = 0;
	}

	public void reset()
	{
		this.stopped = true;
		this.frameCount = 0;
		this.currentFrame = 0;
	}
	


}