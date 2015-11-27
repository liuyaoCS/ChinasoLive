/*
 * Copyright (C) 2011-2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: F:\\project\\work\\ChinasoLive\\app\\src\\main\\rs\\rotate.rs
 */
package com.letv.recorder.utils.rs;

import android.support.v8.renderscript.*;
import android.content.res.Resources;

/**
 * @hide
 */
public class ScriptC_rotate extends ScriptC {
    private static final String __rs_resource_name = "rotate";
    // Constructor
    public  ScriptC_rotate(RenderScript rs) {
        this(rs,
             rs.getApplicationContext().getResources(),
             rs.getApplicationContext().getResources().getIdentifier(
                 __rs_resource_name, "raw",
                 rs.getApplicationContext().getPackageName()));
    }

    public  ScriptC_rotate(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        __ALLOCATION = Element.ALLOCATION(rs);
        __I32 = Element.I32(rs);
        mExportVar_direction = 2;
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_I32;
    private final static int mExportVarIdx_gIn = 0;
    private Allocation mExportVar_gIn;
    public synchronized void set_gIn(Allocation v) {
        setVar(mExportVarIdx_gIn, v);
        mExportVar_gIn = v;
    }

    public Allocation get_gIn() {
        return mExportVar_gIn;
    }

    public Script.FieldID getFieldID_gIn() {
        return createFieldID(mExportVarIdx_gIn, null);
    }

    private final static int mExportVarIdx_gOut = 1;
    private Allocation mExportVar_gOut;
    public synchronized void set_gOut(Allocation v) {
        setVar(mExportVarIdx_gOut, v);
        mExportVar_gOut = v;
    }

    public Allocation get_gOut() {
        return mExportVar_gOut;
    }

    public Script.FieldID getFieldID_gOut() {
        return createFieldID(mExportVarIdx_gOut, null);
    }

    private final static int mExportVarIdx_width = 2;
    private int mExportVar_width;
    public synchronized void set_width(int v) {
        setVar(mExportVarIdx_width, v);
        mExportVar_width = v;
    }

    public int get_width() {
        return mExportVar_width;
    }

    public Script.FieldID getFieldID_width() {
        return createFieldID(mExportVarIdx_width, null);
    }

    private final static int mExportVarIdx_height = 3;
    private int mExportVar_height;
    public synchronized void set_height(int v) {
        setVar(mExportVarIdx_height, v);
        mExportVar_height = v;
    }

    public int get_height() {
        return mExportVar_height;
    }

    public Script.FieldID getFieldID_height() {
        return createFieldID(mExportVarIdx_height, null);
    }

    private final static int mExportVarIdx_direction = 4;
    private int mExportVar_direction;
    public synchronized void set_direction(int v) {
        setVar(mExportVarIdx_direction, v);
        mExportVar_direction = v;
    }

    public int get_direction() {
        return mExportVar_direction;
    }

    public Script.FieldID getFieldID_direction() {
        return createFieldID(mExportVarIdx_direction, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_filter = 1;
    public Script.KernelID getKernelID_filter() {
        return createKernelID(mExportForEachIdx_filter, 1, null, null);
    }

    public void forEach_filter(Allocation ain) {
        forEach_filter(ain, null);
    }

    public void forEach_filter(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_filter, ain, null, null, sc);
    }

}

