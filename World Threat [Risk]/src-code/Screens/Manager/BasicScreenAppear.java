package com.strat7.game.Screens.Manager;

import com.strat7.game.Interfaces.Basics.BoundBasics.BoundWithFrame;
import com.strat7.game.Interfaces.Basics.BoundBasics.Carcass;
import com.strat7.game.Interfaces.Basics.BoundBasics.Frame;
import com.strat7.game.Tutorial.SelectedFrame;

/**
    Basic screen appear implements only appear with SlideFromRight and DisappearWithGroove
    With basic Time {@link AppearParameter}
*/
public class BasicScreenAppear extends AppearParameter{
    public static final int SLIDE_FROM_RIGHT = 0;
    public static final int WITH_GROOVE = 1;
    public static final double STANDARD_APPEAR_TIME = SLOW;
    public static final double STANDARD_DISAPPEAR_TIME = NORMAL;

    private SelectedFrame selectedFrame;

    public BasicScreenAppear(BoundWithFrame appearing) {
        this(appearing, SLIDE_FROM_RIGHT, WITH_GROOVE);
    }
    public BasicScreenAppear(BoundWithFrame appearing,boolean reversed) {
        this(appearing, SLIDE_FROM_RIGHT, WITH_GROOVE);
        setReversed(reversed);
    }
    public BasicScreenAppear(BoundWithFrame appearing, int appearType, int disappearType) {
        super(appearing, STANDARD_APPEAR_TIME, STANDARD_DISAPPEAR_TIME, appearType, disappearType);
    }

    @Override
    protected void initState() {
        super.initState();
        selectedFrame = new SelectedFrame(
                "Interface\\ScreenAppear\\TopCorner.png",
                "Interface\\ScreenAppear\\TopCorner.png",
                "Interface\\ScreenAppear\\BottomCorner.png",
                "Interface\\ScreenAppear\\BottomCorner.png",
                "Interface\\ScreenAppear\\TopEdge.png",
                "Interface\\ScreenAppear\\LateralEdge.png",
                "Interface\\ScreenAppear\\BottomEdge.png",
                "Interface\\ScreenAppear\\LateralEdge.png",
                0, 0, 0, 0, 0, 0
        );
    }

    protected void setAppearPoints() {
        super.setAppearPoints();
        switch (getAppearType()) {
            case SLIDE_FROM_RIGHT:
                appearStart.setPosX (appearTarget.getMainFrame().getLocalWidth());
                break;
            case WITH_GROOVE:
                appearStart.setScale(appearTarget.getLocalScale() * 0.9);
                appearStart.setPosX(appearStart.getLocalPosX() + appearStart.getLocalWidth () * 0.1 / 2);
                appearStart.setPosY(appearStart.getLocalPosY() + appearStart.getLocalHeight() * 0.1 / 2);
                break;

            default:
                break;
        }
    }

    protected void setDisappearPoints() {
        super.setDisappearPoints();
        switch (getDisappearType()) {
            case SLIDE_FROM_RIGHT:
                disappearTarget.setPosX(disappearStart.getMainFrame().getLocalWidth());
                break;
            case WITH_GROOVE:
                disappearTarget.setScale(disappearStart.getLocalScale() * 0.95);
                disappearTarget.setScale(disappearStart.getLocalScale() * 0.95);
                disappearTarget.setPosX (disappearStart.getLocalPosX() + disappearStart.getLocalWidth () * 0.05 / 2);
                disappearTarget.setPosY (disappearStart.getLocalPosY() + disappearStart.getLocalHeight() * 0.05 / 2);
                break;

            default:
                break;
        }
    }

    @Override
    public void appear() {
        selectedFrame.setNewFrame(Frame.MAIN_FRAME,null);
        switch (getAppearType()) {
            case SLIDE_FROM_RIGHT:
                if(getDisappearType() != WITH_GROOVE)
                    return;
                Frame frame = getDisappearTarget().toAbsoluteFrame();
                frame.setWidth(frame.getLocalWidth() * (getAppearTime() - timePassed) / getAppearTime());
                selectedFrame.setNewFrame(frame, null);
                selectedFrame.setAllEdgesWidth(
                        ((float) Math.abs(getDisappearStart().getHeight() - getDisappearTarget().getHeight()) / 2),
                        ((float) Math.abs(getDisappearStart().getWidth() - getDisappearTarget().getWidth()) / 2)
                );
                break;

            case WITH_GROOVE:
                selectedFrame.setNewFrame(getAppearStart(),null);
                selectedFrame.setAllEdgesWidth(
                        ((float) Math.abs(getAppearTarget().getHeight() - getAppearStart().getHeight()) / 2) ,
                        ((float) Math.abs(getAppearTarget().getWidth() - getAppearStart().getWidth()) / 2)
                );
                break;

            case AUTO:
                break;

            default:
                break;
        }


        super.appear();

    }

    @Override
    public void disappear() {

        selectedFrame.setNewFrame(Frame.MAIN_FRAME.toAbsoluteFrame(),null);
        switch (getDisappearType()) {
            case SLIDE_FROM_RIGHT:
                if(getAppearType() != WITH_GROOVE)
                    return;
                Frame frame = getAppearStart().toAbsoluteFrame();
                frame.setWidth(0);
                selectedFrame.setNewFrame(frame, null);
                selectedFrame.setAllEdgesWidth(
                        ((float) Math.abs(getAppearTarget().getHeight() - getAppearStart().getHeight()) / 2),
                        0,
                        ((float) Math.abs(getAppearTarget().getHeight() - getAppearStart().getHeight()) / 2),
                        ((float) Math.abs(getAppearTarget().getWidth() - getAppearStart().getWidth()) / 2)
                );
                break;

            case WITH_GROOVE:
                selectedFrame.setNewFrame(getDisappearTarget().toAbsoluteFrame(),null);
                selectedFrame.setAllEdgesWidth(
                        (float) Math.abs(getDisappearStart().getHeight() - getDisappearTarget().getHeight()) / 2 ,
                        (float) Math.abs(getDisappearStart().getWidth() - getDisappearTarget().getWidth()) / 2
                );
                break;

            case AUTO:
                break;

            default:
                break;
        }

        super.disappear();
    }

    @Override
    public void setInterimPosition() {
        double time = timePassed;
        if(isAppearing()) {
            if (timePassed > getDelayBeforeAppearStart())
                timePassed -= getDelayBeforeAppearStart();
            else
                timePassed = 0;
        }



        if(isDisappearing() && timePassed > getDisappearTime()) {
            getObject().copyLocal(getDisappearTarget());
            if(timePassed > getDisappearTime() + getDelayAfterDisappearEnd()) {
                super.setInterimPosition();
            }
            return;
        }

        super.setInterimPosition();

        setSelectedFramePosition();



        timePassed = time;
    }

    private void setSelectedFramePosition() {
        if(isAppearing()) {
            if(!isReversed()) {
                if(getDisappearType() != WITH_GROOVE)
                    return;
                Carcass frame = selectedFrame.getNewFrame();
                frame.setWidth(getDisappearTarget().getWidth() * (getAppearTime() - timePassed) / getAppearTime());
                selectedFrame.setAllEdgesWidth(
                        ((float) Math.abs(getDisappearStart().getHeight() - getDisappearTarget().getHeight()) / 2),
                        timePassed > getAppearTime() / 2 ? 0 : ((float) (Math.abs(getDisappearStart().getWidth() - getDisappearTarget().getWidth()) * (getAppearTime() / 2 - timePassed) / getAppearTime())),
                        ((float) Math.abs(getDisappearStart().getHeight() - getDisappearTarget().getHeight()) / 2),
                        ((float) Math.abs(getDisappearStart().getWidth() - getDisappearTarget().getWidth()) / 2)
                );
            }
        }
        if(isDisappearing()) {
            if(isReversed()) {
                if(getAppearType() != WITH_GROOVE)
                    return;
                Carcass frame = selectedFrame.getNewFrame();
                frame.setWidth(getAppearStart().getWidth() * (timePassed) / getDisappearTime());
                selectedFrame.setAllEdgesWidth(
                        ((float) Math.abs(getAppearTarget().getHeight() - getAppearStart().getHeight()) / 2),
                        timePassed < getDisappearTime() / 2 ? 0 : ((float) (Math.abs(getAppearTarget().getWidth() - getAppearStart().getWidth()) * (timePassed - getDisappearTime() / 2) / getDisappearTime())),
                        ((float) Math.abs(getAppearTarget().getHeight() - getAppearStart().getHeight()) / 2),
                        ((float) Math.abs(getAppearTarget().getWidth() - getAppearStart().getWidth()) / 2)
                );
            }
        }
    }

    @Override
    public void draw(ProcessingArea anInterface, float delta, boolean considerBorders) {
        if(isAnimated()) {
            incTimePassed(delta);
            setInterimPosition();
        }

        if(!isHiding()) {
            if(isAppearing() && timePassed > getDelayBeforeAppearStart() ||
                    isDisappearing() && timePassed < getDisappearTime())
                selectedFrame.draw(delta, null);
            anInterface.draw(considerBorders);
        }
    }
}
