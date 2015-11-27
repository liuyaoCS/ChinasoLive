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

public class ScriptC_blur extends ScriptC {
    private static final String __rs_resource_name = "blur";
    private Element __ALLOCATION;
    private Element __U32;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_U32;
    private static final int mExportVarIdx_gIn = 0;
    private Allocation mExportVar_gIn;
    private static final int mExportVarIdx_width = 1;
    private long mExportVar_width;
    private static final int mExportVarIdx_height = 2;
    private long mExportVar_height;
    private static final int mExportVarIdx_radius = 3;
    private long mExportVar_radius;
    private static final int mExportForEachIdx_blur_v = 1;
    private static final int mExportForEachIdx_blur_h = 2;

    public ScriptC_blur(RenderScript rs) {
        this(rs, rs.getApplicationContext().getResources(), rs.getApplicationContext().getResources().getIdentifier("blur", "raw", rs.getApplicationContext().getPackageName()));
    }

    public ScriptC_blur(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        this.__ALLOCATION = Element.ALLOCATION(rs);
        this.__U32 = Element.U32(rs);
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

    public synchronized void set_width(long v) {
        if(this.__rs_fp_U32 != null) {
            this.__rs_fp_U32.reset();
        } else {
            this.__rs_fp_U32 = new FieldPacker(4);
        }

        this.__rs_fp_U32.addU32(v);
        this.setVar(1, this.__rs_fp_U32);
        this.mExportVar_width = v;
    }

    public long get_width() {
        return this.mExportVar_width;
    }

    public FieldID getFieldID_width() {
        return this.createFieldID(1, (Element) null);
    }

    public synchronized void set_height(long v) {
        if(this.__rs_fp_U32 != null) {
            this.__rs_fp_U32.reset();
        } else {
            this.__rs_fp_U32 = new FieldPacker(4);
        }

        this.__rs_fp_U32.addU32(v);
        this.setVar(2, this.__rs_fp_U32);
        this.mExportVar_height = v;
    }

    public long get_height() {
        return this.mExportVar_height;
    }

    public FieldID getFieldID_height() {
        return this.createFieldID(2, (Element) null);
    }

    public synchronized void set_radius(long v) {
        if(this.__rs_fp_U32 != null) {
            this.__rs_fp_U32.reset();
        } else {
            this.__rs_fp_U32 = new FieldPacker(4);
        }

        this.__rs_fp_U32.addU32(v);
        this.setVar(3, this.__rs_fp_U32);
        this.mExportVar_radius = v;
    }

    public long get_radius() {
        return this.mExportVar_radius;
    }

    public FieldID getFieldID_radius() {
        return this.createFieldID(3, (Element) null);
    }

    public KernelID getKernelID_blur_v() {
        return this.createKernelID(1, 33, (Element) null, (Element) null);
    }

    public void forEach_blur_v(Allocation ain) {
        this.forEach_blur_v(ain, (LaunchOptions) null);
    }

    public void forEach_blur_v(Allocation ain, LaunchOptions sc) {
        if(!ain.getType().getElement().isCompatible(this.__U32)) {
            throw new RSRuntimeException("Type mismatch with U32!");
        } else {
            this.forEach(1, ain, (Allocation)null, (FieldPacker)null, sc);
        }
    }

    public KernelID getKernelID_blur_h() {
        return this.createKernelID(2, 33, (Element) null, (Element) null);
    }

    public void forEach_blur_h(Allocation ain) {
        this.forEach_blur_h(ain, (LaunchOptions) null);
    }

    public void forEach_blur_h(Allocation ain, LaunchOptions sc) {
        if(!ain.getType().getElement().isCompatible(this.__U32)) {
            throw new RSRuntimeException("Type mismatch with U32!");
        } else {
            this.forEach(2, ain, (Allocation)null, (FieldPacker)null, sc);
        }
    }
}
