//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

import android.content.res.Resources;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.FieldPacker;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptC;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;
import android.support.v8.renderscript.Script.LaunchOptions;

public class ScriptC_rotate extends ScriptC {
    private static final String __rs_resource_name = "rotate";
    private Element __ALLOCATION;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_I32;
    private static final int mExportVarIdx_gIn = 0;
    private Allocation mExportVar_gIn;
    private static final int mExportVarIdx_gOut = 1;
    private Allocation mExportVar_gOut;
    private static final int mExportVarIdx_width = 2;
    private int mExportVar_width;
    private static final int mExportVarIdx_height = 3;
    private int mExportVar_height;
    private static final int mExportVarIdx_direction = 4;
    private int mExportVar_direction;
    private static final int mExportForEachIdx_filter = 1;

    public ScriptC_rotate(RenderScript rs) {
        this(rs, rs.getApplicationContext().getResources(), rs.getApplicationContext().getResources().getIdentifier("rotate", "raw", rs.getApplicationContext().getPackageName()));
    }

    public ScriptC_rotate(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        this.__ALLOCATION = Element.ALLOCATION(rs);
        this.__I32 = Element.I32(rs);
        this.mExportVar_direction = 2;
        this.__U8_4 = Element.U8_4(rs);
    }

    public synchronized void set_gIn(Allocation v) {
        this.setVar(0, v);
        this.mExportVar_gIn = v;
    }

    public Allocation get_gIn() {
        return this.mExportVar_gIn;
    }

    public FieldID getFieldID_gIn() {
        return this.createFieldID(0, (Element) null);
    }

    public synchronized void set_gOut(Allocation v) {
        this.setVar(1, v);
        this.mExportVar_gOut = v;
    }

    public Allocation get_gOut() {
        return this.mExportVar_gOut;
    }

    public FieldID getFieldID_gOut() {
        return this.createFieldID(1, (Element) null);
    }

    public synchronized void set_width(int v) {
        this.setVar(2, v);
        this.mExportVar_width = v;
    }

    public int get_width() {
        return this.mExportVar_width;
    }

    public FieldID getFieldID_width() {
        return this.createFieldID(2, (Element) null);
    }

    public synchronized void set_height(int v) {
        this.setVar(3, v);
        this.mExportVar_height = v;
    }

    public int get_height() {
        return this.mExportVar_height;
    }

    public FieldID getFieldID_height() {
        return this.createFieldID(3, (Element) null);
    }

    public synchronized void set_direction(int v) {
        this.setVar(4, v);
        this.mExportVar_direction = v;
    }

    public int get_direction() {
        return this.mExportVar_direction;
    }

    public FieldID getFieldID_direction() {
        return this.createFieldID(4, (Element) null);
    }

    public KernelID getKernelID_filter() {
        return this.createKernelID(1, 57, (Element) null, (Element) null);
    }

    public void forEach_filter(Allocation ain) {
        this.forEach_filter(ain, (LaunchOptions) null);
    }

    public void forEach_filter(Allocation ain, LaunchOptions sc) {
        if(!ain.getType().getElement().isCompatible(this.__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        } else {
            this.forEach(1, ain, (Allocation)null, (FieldPacker)null, sc);
        }
    }
}
