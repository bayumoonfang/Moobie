package com.example.moobie.Function;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ExpandableHeightLinearLayout extends LinearLayout

    {


        public ExpandableHeightLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ExpandableHeightLinearLayout(Context context) {
            super(context);
        }

        public ExpandableHeightLinearLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }



                @Override
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
                {
                    // For simple implementation, or internal size is always 0.
                    // We depend on the container to specify the layout size of
                    // our view. We can't really know what it is since we will be
                    // adding and removing different arbitrary views and do not
                    // want the layout to change as this happens.
                    setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

                    // Children are just made to fill our space.

                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                    if (heightMeasureSpec > widthMeasureSpec) {//没用，在显示前height和width都为0
                        heightMeasureSpec = widthMeasureSpec;
                    }
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);



                }

        }


