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

package org.eyeseetea.uicapp.presentation.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.AttributeSet;

import org.eyeseetea.uicapp.R;

import androidx.appcompat.widget.AppCompatEditText;

public class EditCard extends AppCompatEditText {
    private Context context = getContext();
    private String mfontName = context.getString(R.string.normal_font);
    private AssetManager assetManager = context.getAssets();
    private TypedArray a;
    private Typeface font;

    public EditCard(Context context) {
        super(context);
        init(null, 0);
    }

    public EditCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EditCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    public void init(AttributeSet attrs, int defStyle) {
        if(isInEditMode()){
            return;
        }
        // Load attributes
        if (attrs != null) {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.TextCard, defStyle, 0);
            try {
                mfontName = a.getString(R.styleable.EditCard_eFontName);
                if (mfontName != null) {
                    font = Typeface.createFromAsset(assetManager, "fonts/" + mfontName);
                    setTypeface(font);
                }

            } finally {
                a.recycle();
            }
        }
        disableTextSuggestions();
    }

    private void disableTextSuggestions() {
        //InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS does not seem to work as expected on all keyboards
        // whereas InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD has the drawback that it also
        // disables toggling the language in the keyboard and the swipe gesture to add the text.
        setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }


    /**
     * Gets the mDimension attribute value.
     *
     * @return The mDimension attribute value.
     */
    public String getmFontName() {
        return mfontName;
    }

}

