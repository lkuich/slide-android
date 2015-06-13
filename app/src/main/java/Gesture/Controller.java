package Gesture;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.j03.mobileinput.Canvas.CanvasActivity;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;

import java.util.ArrayList;
import java.util.List;

import Settings.AppSettings;
import Gesture.Binding.Actions.Range;
import Gesture.Binding.Actions.Toggle;
import Gesture.Binding.Actions.Trigger;
import Layout.Enums.PositioningMode;
import Layout.Enums.PressureMode;

public abstract class Controller
    extends View // TODO: Change to SpenSurfaceView
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
    SpenSurfaceView.OnTouchListener, SpenSurfaceView.OnHoverListener
{
    private GestureDetector gesture;

    private int[] saved;
    private List<InputSource> source;

    private int touchPoints;
    private boolean isTouchDown;
    private boolean pressureToggle;

    public Controller(final Context context)
    {
        super(context);

        gesture = new GestureDetector(this);

        saved = new int[2];
        source = new ArrayList<>();
        source.add(new InputSource());

        touchPoints = 0;

        pressureToggle = false;

        this.setOnTouchListener(this);
        this.setOnHoverListener(this);
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event)
    {
        setPressureToggle(event.getPressure());
        AppSettings.getInstance().getConnectionManager().send(Trigger.TAP);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent)
    {
        return true;
    }

    int doubleTapIncrement = 0;
    @Override
    public boolean onDoubleTapEvent(final MotionEvent event)
    {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                setMouseDown();
                doubleTapIncrement = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                doubleTapIncrement++;
                break;

            case MotionEvent.ACTION_UP:
                setMouseUp();
                if (doubleTapIncrement < 5)
                {
                    AppSettings.getInstance().getConnectionManager().send(
                        Trigger.TAP);
                }
                doubleTapIncrement = 0;
                break;
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent)
    {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) { }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent)
    {
        return true;
    }

    @Override
    public boolean onScroll(
        MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent)
    {
        final int action = motionEvent.getAction();
        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                if (doubleTapIncrement == 0)
                {
                    if (AppSettings.getInstance().getSettingsElements().getRightClickEnabled())
                    {
                        AppSettings.getInstance().getConnectionManager().send(Trigger.LONG_HOLD);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onFling(
        MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2)
    {
        return true;
    }

    @Override
    public boolean onHover(View view, MotionEvent event)
    {
        getSource(0).setCoordinates((int) event.getX(0), (int) event.getY(0));

        if (AppSettings.getInstance().getSystemSettings().getKitKat())
        {
            CanvasActivity.hideUi(CanvasActivity.mainActivityContext);
        }
        return movePointer(event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        getSource(0).setCoordinates((int) event.getX(0), (int) event.getY(0));
        return movePointer(event);
    }

    private int savedTravel = 0; // Multi touch
    private int breakpoint = 0; // Multi touch
    protected boolean movePointer(final MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                setSaved((int) event.getX(0), (int) event.getY(0));
                AppSettings.getInstance().getConnectionManager().send(Toggle.MOVING);

                setTouchDown(true);

                if (ControllerToggles.getPreviousCommand() == MotionEvent.ACTION_HOVER_EXIT)
                {
                    setMouseDown();
                }
                break;

            case MotionEvent.ACTION_HOVER_ENTER:
                // Standard Movement
                setSaved((int) event.getX(0), (int) event.getY(0));
                AppSettings.getInstance().getConnectionManager().send(Toggle.MOVING);

                setTouchDown(true);

                setMouseUp();

                break;

            case MotionEvent.ACTION_POINTER_DOWN: // Multitouch
                getSourceList().add(new InputSource());
                setTouchPoints(getSourceList().size() - 1);

                if (event.getX(getTouchPoints()) <= 4 && event.getY(getTouchPoints()) <= 4)
                {
                    getSource(getTouchPoints()).setCoordinates(
                        (int) event.getX(getTouchPoints()),
                        (int) event.getY(getTouchPoints()));
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (getTouchPoints() > 0)
                {
                    getSourceList().remove(getTouchPoints()); // Remove last pointer
                    setTouchPoints(getTouchPoints() - 1);

                    this.breakpoint = 0;
                    this.savedTravel = 0;
                    setTouchDown(false);
                }
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_HOVER_MOVE:
                if (getTouchDown()) // Make sure there is touch input is down
                {
                    if (getTouchPoints() == 0)
                    {
                        // Moves the pointer
                        final short[] pack = new short[2];
                        if (AppSettings.getInstance().getSettingsElements().getPositioningMode()
                            == PositioningMode.RELATIVE)
                        {
                            pack[0] = (short) (getSource(0).getX() - getSavedX());
                            pack[1] = (short) (getSource(0).getY() - getSavedY());
                        } else
                        {
                            pack[0] = (short) getSource(0).getX();
                            pack[1] = (short) getSource(0).getY();
                        }
                        AppSettings.getInstance().getConnectionManager().send(pack);

                        if (AppSettings.getInstance().getSettingsElements().getPressureClickEnabled())
                        {
                            final float pressure = event.getPressure();
                            if (AppSettings.getInstance().getSettingsElements().getPressureMode() == PressureMode.INITIAL_TOUCH)
                            {
                                if (pressure > AppSettings.getInstance().getSettingsElements().getPressureSensitivity())
                                {
                                    setMouseDown();
                                } else
                                {
                                    setMouseUp();
                                }
                            } else if (AppSettings.getInstance().getSettingsElements().getPressureMode() == PressureMode.TOGGLE)
                            {
                                if (getPressureToggle())
                                {
                                    setMouseDown();
                                }
                            }
                        }

                    } else if (getTouchPoints() == 1)
                    {
                        if (AppSettings.getInstance().getSettingsElements().getZoomEnabled())
                        {
                            final int moveDistance = getDistance(
                                getSource(0).getX(),
                                getSource(1).getX());

                            if (this.savedTravel != 0)
                            {
                                if (moveDistance >= this.savedTravel)
                                {
                                    this.breakpoint++;
                                    if (this.breakpoint == 8)
                                    {
                                        AppSettings.getInstance().getConnectionManager().send(Range.ZOOM_OUT);
                                        this.breakpoint = 0;
                                    }
                                } else if (moveDistance <= this.savedTravel)
                                {
                                    this.breakpoint++;
                                    if (this.breakpoint == 8)
                                    {
                                        AppSettings.getInstance().getConnectionManager().send(Range.ZOOM_IN);
                                        this.breakpoint = 0;
                                    }
                                }
                            }
                            this.savedTravel = moveDistance;
                        }
                    } else if (getTouchPoints() == 2)
                    {
                        if (AppSettings.getInstance().getSettingsElements().getScrollEnabled())
                        {
                            final int scrollDistance = getSource(0).getY();

                            if (this.savedTravel != 0)
                            {
                                if (scrollDistance >= this.savedTravel)
                                {
                                    breakpoint++;
                                    if (breakpoint == 3)
                                    {
                                        AppSettings.getInstance().getConnectionManager()
                                            .send(Range.SCROLL_DOWN);
                                        this.breakpoint = 0;
                                    }
                                } else if (scrollDistance <= this.savedTravel)
                                {
                                    this.breakpoint++;
                                    if (this.breakpoint == 3)
                                    {
                                        AppSettings.getInstance().getConnectionManager()
                                            .send(Range.SCROLL_UP);
                                        this.breakpoint = 0;
                                    }
                                }
                            }
                            this.savedTravel = getSource(0).getY();
                        }
                    }
                }
                break;
        }
        ControllerToggles.setPreviousCommand(event.getAction());
        return this.gesture.onTouchEvent(event);
    }

    private List<InputSource> getSourceList()
    {
        return this.source;
    }

    protected InputSource getSource(final int index)
    {
        return this.source.get(index);
    }

    private boolean getPressureToggle()
    {
        return this.pressureToggle;
    }

    private int getTouchPoints()
    {
        return this.touchPoints;
    }

    protected synchronized int getSavedX()
    {
        return this.saved[0];
    }

    protected synchronized int getSavedY()
    {
        return this.saved[1];
    }

    private int getDistance(final int point1, final int point2)
    {
        if (point1 >= point2)
        {
            return point1 - point2;
        } else if (point2 >= point1)
        {
            return point2 - point1;
        } else
        {
            return 0;
        }
    }

    private boolean getTouchDown()
    {
        return this.isTouchDown;
    }

    private void setPressureToggle(final float pressure)
    {
        if (AppSettings.getInstance().getSettingsElements().getPressureClickEnabled())
        {
            if (AppSettings.getInstance().getSettingsElements().getPressureMode() == PressureMode.TOGGLE)
            {
                if (pressure > AppSettings.getInstance().getSettingsElements().getPressureSensitivity())
                {
                    this.pressureToggle = !this.pressureToggle;
                }
            }
        }
    }

    private void setTouchPoints(final int touchPoints)
    {
        this.touchPoints = touchPoints;
    }

    private void setSaved(final int x, final int y)
    {
        this.saved[0] = x;
        this.saved[1] = y;
    }

    private void setTouchDown(final boolean touchDown)
    {
        this.isTouchDown = touchDown;
    }

    private void setMouseDown()
    {
        AppSettings.getInstance().getConnectionManager().send(Toggle.MOUSE_DOWN);
        AppSettings.getInstance().getCanvasSettings().getPaint().setColor(Color.RED);
    }

    private void setMouseUp()
    {
        AppSettings.getInstance().getConnectionManager().send(Toggle.MOUSE_UP);
        AppSettings.getInstance().getCanvasSettings().getPaint().setColor(Color.WHITE);
    }
}
