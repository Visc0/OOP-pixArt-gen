package it.unibo.pixArt.controller.animation;

import java.util.List;
import it.unibo.pixArt.controller.SimpleController;
import it.unibo.pixArt.model.historyframe.HistoryFrame;
import it.unibo.pixArt.utilities.ImagePrinter;
import it.unibo.pixArt.view.animation.AnimationView;


public class AnimationControllerImpl extends SimpleController implements AnimationController {
    private Boolean isRunning = false;
    private Directions animationDirection = Directions.FORWARD;
    private int index = 0;

    public AnimationControllerImpl() {
    }

    private class Animator extends Thread {
        @Override
        public void run() {
            while(isRunning){
                final HistoryFrame currentFrame = getCurrentImage();
                getAnimationView().displayImage(currentFrame.getPath());
                try {
                    Thread.sleep(currentFrame.getAnimationDuration());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setAnimationDirection(final String newDir) {
        this.animationDirection = Directions.getListDirections().stream().filter(e -> e.getName() == newDir).findAny().get();
    }

    @Override
    public void setFrameDuration(final int frameIndex, final int duration) {
        this.getModel().getProject().getAllHistoryFrames().get(frameIndex).setAnimationDuration(duration);
    }

    @Override
    public void setAnimationIsRunning() {
        if(!this.isRunning) {
            this.index = 0;
            final Thread th = new Animator();
            th.start();
        }
        this.isRunning = !this.isRunning;
    }

    @Override
    public List<String> getListSizes() {
        return PreviewSizes.getListSizes();
    }

    @Override
    public List<Directions> getListDirections() {
        return Directions.getListDirections();
    }

    @Override
    public HistoryFrame getCurrentImage() { 
        if(this.index == getHistoryFrames().size()  || this.index == 0) {
            this.index = 0;
        }
        final int prev = this.index;
        this.index = this.index + animationDirection.getValue();
        return getHistoryFrames().get(prev);
    }

    @Override
    public List<HistoryFrame> getHistoryFrames() {
        return this.getModel().getProject().getAllHistoryFrames();
    }
    
    @Override
    public boolean getAnimationIsRunning() {
        return this.isRunning;
    }

    private AnimationView getAnimationView() {
        return (AnimationView) this.getView();
    }

    @Override
    public void saveProject() {
        ImagePrinter.getInstance().printAllFrames(getModel().getProject());
    }
}
