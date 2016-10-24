/*
 * Copyright (c) 2015.
 *
 * This file is part of QIS Surveillance App.
 *
 *  QIS Surveillance App is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  QIS Surveillance App is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with QIS Surveillance App.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.eyeseetea.uicapp.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import org.eyeseetea.uicapp.R;

/**
 * Created by adrian on 30/05/15.
 */
public class CustomRadioButton extends RadioButton {
    private Context context = getContext();
    private String mfontName = context.getString(R.string.normal_font);
    private AssetManager assetManager = context.getAssets();
    private TypedArray a;
    private Typeface font;

    public CustomRadioButton(Context context) {
        super(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Initializing method. Sets font name and font size depending on the styled attributes selected
     * @param attrs
     * @param defStyle
     */
    public void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        if (attrs != null) {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRadioButton, defStyle, 0);
            try {
                mfontName = a.getString(R.styleable.CustomRadioButton_rFontName);
                if (mfontName != null) {
                    font = Typeface.createFromAsset(assetManager, "fonts/" + mfontName);
                    setTypeface(font);
                }
            } finally {
                a.recycle();
            }
        }
    }

    /**
     * Set the Object font name. This must be a valid font placed in fonts subfolder, inside the resources of the app
     * @param fontName
     */
    public void updateFontName(String fontName){
        if (fontName != null){
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
            setTypeface(font);
            mfontName = fontName;
        }
    }

}
