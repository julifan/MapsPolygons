package rs.pupin.custompolyline2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class iImageView extends ImageView implements View.OnTouchListener {


    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;

    private int mode = NONE;

    private PointF mStartPoint = new PointF();
    private PointF mMiddlePoint = new PointF();
    private Point mBitmapMiddlePoint = new Point();

    private float oldDist = 1f;
    private float matrixValues[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private float scale;
    private float oldEventX = 0;
    private float oldEventY = 0;
    private float oldStartPointX = 0;
    private float oldStartPointY = 0;
    private int mViewWidth = -1;
    private int mViewHeight = -1;
    private int mBitmapWidth = -1;
    private int mBitmapHeight = -1;
    private boolean mDraggable = false;


    public iImageView(Context context) {
        this(context, null, 0);
    }

    public iImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public iImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnTouchListener(this);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            setImageBitmap(bitmap);

            mBitmapWidth = bitmap.getWidth();
            mBitmapHeight = bitmap.getHeight();
            mBitmapMiddlePoint.x = (mViewWidth / 2) - (mBitmapWidth / 2);
            mBitmapMiddlePoint.y = (mViewHeight / 2) - (mBitmapHeight / 2);

            matrix.postTranslate(mBitmapMiddlePoint.x, mBitmapMiddlePoint.y);
            this.setImageMatrix(matrix);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                mStartPoint.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mMiddlePoint, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    drag(event);
                } else if (mode == ZOOM) {
                    zoom(event);
                }
                break;
        }

        return true;
    }


    public void drag(MotionEvent event) {
        matrix.getValues(matrixValues);

        float left = matrixValues[2];
        float top = matrixValues[5];
        float bottom = (top + (matrixValues[0] * mBitmapHeight)) - mViewHeight;
        float right = (left + (matrixValues[0] * mBitmapWidth)) - mViewWidth;

        float eventX = event.getX();
        float eventY = event.getY();
        float spacingX = eventX - mStartPoint.x;
        float spacingY = eventY - mStartPoint.y;
        float newPositionLeft = (left < 0 ? spacingX : spacingX * -1) + left;
        float newPositionRight = (spacingX) + right;
        float newPositionTop = (top < 0 ? spacingY : spacingY * -1) + top;
        float newPositionBottom = (spacingY) + bottom;
        boolean x = true;
        boolean y = true;

        if (newPositionRight < 0.0f || newPositionLeft > 0.0f) {
            if (newPositionRight < 0.0f && newPositionLeft > 0.0f) {
                x = false;
            } else {
                eventX = oldEventX;
                mStartPoint.x = oldStartPointX;
            }
        }
        if (newPositionBottom < 0.0f || newPositionTop > 0.0f) {
            if (newPositionBottom < 0.0f && newPositionTop > 0.0f) {
                y = false;
            } else {
                eventY = oldEventY;
                mStartPoint.y = oldStartPointY;
            }
        }

        if (mDraggable) {
            matrix.set(savedMatrix);
            matrix.postTranslate(x ? eventX - mStartPoint.x : 0, y ? eventY - mStartPoint.y : 0);
            this.setImageMatrix(matrix);
            if (x) oldEventX = eventX;
            if (y) oldEventY = eventY;
            if (x) oldStartPointX = mStartPoint.x;
            if (y) oldStartPointY = mStartPoint.y;
        }

    }

    public void zoom(MotionEvent event) {
        matrix.getValues(matrixValues);

        float newDist = spacing(event);
        float bitmapWidth = matrixValues[0] * mBitmapWidth;
        float bimtapHeight = matrixValues[0] * mBitmapHeight;
        boolean in = newDist > oldDist;

        if (!in && matrixValues[0] < 0.05) {
            return;
        }
        if (bitmapWidth > mViewWidth || bimtapHeight > mViewHeight) {
            mDraggable = true;
        } else {
            //mDraggable = false;
        }

        float midX = (mViewWidth / 2);
        float midY = (mViewHeight / 2);

        matrix.set(savedMatrix);
        scale = newDist / oldDist;
        matrix.postScale(scale, scale, bitmapWidth > mViewWidth ? mMiddlePoint.x : midX, bimtapHeight > mViewHeight ? mMiddlePoint.y : midY);

        this.setImageMatrix(matrix);

    }


    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


}

/*import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class iImageView extends ImageView
{
    static final int NONE_MODE = 0;
    static final int DRAG_MODE = 1;
    static final int ZOOM_MODE = 2;
    int _mode = NONE_MODE;

    Matrix _matrix = new Matrix();
    PointF _previousPoint = new PointF();
    PointF _startPoint = new PointF();
    float _currentScale = 1f;
    float _minScale = 0.3f;
    float _maxScale = 3f;
    float[] _arrayOf9Floats;
    float _bitmapWidth, _bitmapHeight,_displayWidth, _displayHeight;
    ScaleGestureDetector _scaleDetector;
    Context _context;

    public iImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        super.setClickable(true);
        _context = context;
        _scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        _arrayOf9Floats = new float[9];
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return handleTouch(v, event);
            }
        });
    }

    private boolean handleTouch(View v, MotionEvent event)
    {
        _scaleDetector.onTouchEvent(event);
        //Contrary to how this line looks, the matrix is not setting the values from the arrayOf9Floats, but rather taking the
        //matrix values and assigning them into the arrayOf9Floats. I extremely dislike this syntax and I think
        //it should have been written as _arrayOf9Floats = _matrix.getValues() but that's Android for you!!!
        _matrix.getValues(_arrayOf9Floats);

        //Look at https://youtu.be/IiXB6tYtY4w?t=4m12s , it shows scale, rotate, and translate matrices
        //If you look at the translate matrix, you'll see that the 3rd and 6th values are the values which represent x and y translations respectively
        //this corresponds to the 2nd and 5th values in the array and hence why the MTRANS_X and MTRANS_Y have the constants 2 and 5 respectively
        float xTranslate = _arrayOf9Floats[Matrix.MTRANS_X];
        float yTranslate = _arrayOf9Floats[Matrix.MTRANS_Y];
        PointF currentEventPoint = new PointF(event.getX(), event.getY());
        switch (event.getAction())
        {
            //First finger down only
            case MotionEvent.ACTION_DOWN:
                _previousPoint.set(event.getX(), event.getY());
                _startPoint.set(_previousPoint);
                _mode = DRAG_MODE;
                break;
            //Second finger down
            case MotionEvent.ACTION_POINTER_DOWN:
                _previousPoint.set(event.getX(), event.getY());
                _startPoint.set(_previousPoint);
                _mode = ZOOM_MODE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (_mode == ZOOM_MODE || _mode == DRAG_MODE )
                {
                    float deltaX = currentEventPoint.x - _previousPoint.x;
                    float deltaY = currentEventPoint.y - _previousPoint.y;
                    //In matrix terms, going right is + and going left is +
                    //Moving the image right past 0 means it will show alpha space on the left so we dont want that
                    //Keep in mind this is a TOP LEFT pivot point, so we dont want the top left to be past 0 lest we have alpha space
                    if(xTranslate + deltaX > 0)
                    {
                        //get absolute of how much into the negative we would have gone
                        float excessDeltaX = Math.abs(xTranslate + deltaX);
                        //take that excess away from deltaX so X wont got less than 0 after the translation
                        deltaX = deltaX - excessDeltaX;
                    }

                    //Going left we dont want the negative value to be less than the negative width of the sprite, lest we get alpha space on the right
                    //The width is going to be the width of the bitmap * scale and we want the - of it because we are checking for left movement
                    //We also need to account for the width of the DISPLAY CONTAINER (i.e. _displayWidth) so that gets subtracted
                    //i.e. we want the max scroll width value
                    float maxScrollableWidth = _bitmapWidth * _currentScale - _displayWidth;
                    if(xTranslate + deltaX < -maxScrollableWidth)
                    {
                        //this forces the max possible translate to always match the - of maxScrollableWidth
                        deltaX = -maxScrollableWidth - xTranslate;
                    }

                    //repeat for Y
                    if(yTranslate + deltaY > 0)
                    {
                        float excessDeltaY = Math.abs(yTranslate + deltaY);
                        deltaY = deltaY - excessDeltaY;
                    }

                    float maxScrollableHeight = _bitmapHeight * _currentScale - _displayWidth;
                    if(yTranslate + deltaY < -maxScrollableHeight)
                    {
                        //this forces the max possible translate to always match the - of maxScrollableWidth
                        deltaY = -maxScrollableHeight - yTranslate;
                    }

                    _matrix.postTranslate(deltaX, deltaY);
                    _matrix.getValues(_arrayOf9Floats);
                    //System.out.println(_matrix);

                    _previousPoint.set(currentEventPoint.x, currentEventPoint.y);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                _mode = NONE_MODE;
                break;
        }
        setImageMatrix(_matrix);
        invalidate();
        return true;
    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
        _bitmapWidth = bm.getWidth();
        _bitmapHeight = bm.getHeight();
        invalidate();
    }

    @Override
    public void setImageResource(int resid)
    {
        Bitmap bitmapImage = BitmapFactory.decodeResource(_context.getResources(), resid);
        setImageBitmap(bitmapImage);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            _mode = ZOOM_MODE;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            float scaleFactor = detector.getScaleFactor();
            float originalScale = _currentScale;
            _currentScale *= scaleFactor;
            //Zoom in too much
            if (_currentScale > _maxScale) {
                _currentScale = _maxScale;
                scaleFactor = _maxScale / originalScale;
            }//Zoom out too much
            else if (_currentScale < _minScale) {
                _currentScale = _minScale;
                scaleFactor = _minScale / originalScale;
            }
            _matrix.postScale(scaleFactor,scaleFactor);

            return true;
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        _displayWidth = MeasureSpec.getSize(widthMeasureSpec);
        _displayHeight = MeasureSpec.getSize(heightMeasureSpec);
        adjustScale();
    }

    private void adjustScale()
    {
        //Fit to display bounds with NO alpha space
        float scale;
        float scaleX =  _displayWidth / _bitmapWidth;
        float scaleY = _displayHeight / _bitmapHeight;
        scale = Math.max(scaleX, scaleY);
        _matrix.setScale(scale, scale);
        setImageMatrix(_matrix);
        _currentScale = scale;
        _minScale = scale;
    }

    public void setMaxZoom(float maxZoom){_maxScale = maxZoom;}
    public void setMinZoom(float minZoom) {_minScale = minZoom;}
}*/