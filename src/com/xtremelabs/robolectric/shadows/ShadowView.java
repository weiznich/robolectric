package com.xtremelabs.robolectric.shadows;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.xtremelabs.robolectric.res.ViewLoader;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;
import com.xtremelabs.robolectric.util.RealObject;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static java.lang.Boolean.TRUE;

/**
 * Shadow implementation of {@code View} that simulates the behavior of this class. Supports listeners, focusability
 * (but not focus order), resource loading, visibility, tags, and tracks the size and shape of the view.
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(View.class)
public class ShadowView {
    @RealObject protected View realView;

    private int id;
    ShadowView parent;
    private Context context;
    private boolean selected;
    private View.OnClickListener onClickListener;
    private Object tag;
    private boolean enabled = true;
    private int visibility = View.VISIBLE;
    int left;
    int top;
    int right;
    int bottom;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(0, 0);
    private Map<Integer, Object> tags = new HashMap<Integer, Object>();
    private boolean clickable;
    protected boolean focusable;
    boolean focusableInTouchMode;
    private int backgroundResourceId = -1;
    protected View.OnKeyListener onKeyListener;
    private boolean isFocused;
    private View.OnFocusChangeListener onFocusChangeListener;
    private boolean wasInvalidated;
    private View.OnTouchListener onTouchListener;
    private AttributeSet attributeSet;

    public void __constructor__(Context context) {
        this.context = context;
    }

    public void __constructor__(Context context, AttributeSet attributeSet) {
        this.attributeSet = attributeSet;
        __constructor__(context);
        applyIdAttribute();
        applyVisibilityAttribute();
    }

    @Implementation
    public void setId(int id) {
        this.id = id;
    }

    @Implementation
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    /**
     * Also sets focusable in touch mode to false if {@code focusable} is false, which is the Android behavior.
     *
     * @param focusable the new status of the {@code View}'s focusability
     */
    @Implementation
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
        if (!focusable) {
            setFocusableInTouchMode(false);
        }
    }

    @Implementation
    public final boolean isFocusableInTouchMode() {
        return focusableInTouchMode;
    }

    /**
     * Also sets focusable to true if {@code focusableInTouchMode} is true, which is the Android behavior.
     *
     * @param focusableInTouchMode the new status of the {@code View}'s touch mode focusability
     */
    @Implementation
    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        this.focusableInTouchMode = focusableInTouchMode;
        if (focusableInTouchMode) {
            setFocusable(true);
        }
    }

    @Implementation
    public boolean isFocusable() {
        return focusable;
    }

    @Implementation
    public int getId() {
        return id;
    }

    /**
     * Simulates the inflating of the requested resource.
     *
     * @param context  the context from which to obtain a layout inflater
     * @param resource the ID of the resource to inflate
     * @param root     the {@code ViewGroup} to add the inflated {@code View} to
     * @return the inflated View
     */
    @Implementation
    public static View inflate(Context context, int resource, ViewGroup root) {
       return ShadowLayoutInflater.from(context).inflate(resource, root);
    }

    /**
     * Finds this {@code View} if it's ID is passed in, returns {@code null} otherwise
     *
     * @param id the id of the {@code View} to find
     * @return the {@code View}, if found, {@code null} otherwise
     */
    @Implementation
    public View findViewById(int id) {
        if (id == this.id) {
            return realView;
        }

        return null;
    }

    @Implementation
    public View getRootView() {
        ShadowView root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root.realView;
    }

    @Implementation
    public ViewGroup.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    @Implementation
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        layoutParams = params;
    }

    @Implementation
    public final ViewParent getParent() {
        return parent == null ? null : (ViewParent) parent.realView;
    }

    @Implementation
    public final Context getContext() {
        return context;
    }

    @Implementation
    public Resources getResources() {
        return context.getResources();
    }

    @Implementation
    public void setBackgroundResource(int backgroundResourceId) {
        this.backgroundResourceId = backgroundResourceId;
    }

    @Implementation
    public int getVisibility() {
        return visibility;
    }

    @Implementation
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Implementation
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Implementation
    public boolean isSelected() {
        return this.selected;
    }

    @Implementation
    public boolean isEnabled() {
        return this.enabled;
    }

    @Implementation
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Implementation
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Implementation
    public boolean performClick() {
        if (onClickListener != null) {
            onClickListener.onClick(realView);
            return true;
        } else {
            return false;
        }
    }

    @Implementation
    public void setOnKeyListener(View.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    @Implementation
    public Object getTag() {
        return this.tag;
    }

    @Implementation
    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Implementation
    public final int getHeight() {
        return bottom - top;
    }

    @Implementation
    public final int getWidth() {
        return right - left;
    }

    @Implementation
    public final int getMeasuredWidth() {
        return getWidth();
    }

    @Implementation
    public final void layout(int l, int t, int r, int b) {
        left = l;
        top = t;
        right = r;
        bottom = b;

// todo:       realView.onLayout();
    }

    @Implementation
    public void setPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
    }

    @Implementation
    public int getPaddingTop() {
        return paddingTop;
    }

    @Implementation
    public int getPaddingLeft() {
        return paddingLeft;
    }

    @Implementation
    public int getPaddingRight() {
        return paddingRight;
    }

    @Implementation
    public int getPaddingBottom() {
        return paddingBottom;
    }

    @Implementation
    public Object getTag(int key) {
        return tags.get(key);
    }

    @Implementation
    public void setTag(int key, Object value) {
        tags.put(key, value);
    }

    @Implementation
    public final boolean requestFocus() {
        return requestFocus(View.FOCUS_DOWN);
    }

    @Implementation
    public final boolean requestFocus(int direction) {
        setViewFocus(true);
        return true;
    }

    public void setViewFocus(boolean hasFocus) {
        this.isFocused = hasFocus;
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(realView, hasFocus);
        }
    }

    @Implementation
    public boolean isFocused() {
        return isFocused;
    }

    @Implementation
    public boolean hasFocus() {
        return isFocused;
    }

    @Implementation
    public void clearFocus() {
        setViewFocus(false);
    }

    @Implementation
    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        onFocusChangeListener = listener;
    }

    @Implementation
    public void invalidate() {
        wasInvalidated = true;
    }

    @Implementation
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Implementation
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (onTouchListener != null) {
            return onTouchListener.onTouch(realView, event);
        }
        return false;
    }

    /**
     * Returns a string representation of this {@code View}. Unless overridden, it will be an empty string.
     *
     * Robolectric extension.
     */
    public String innerText() {
        return "";
    }

    /**
     * Dumps the status of this {@code View} to {@code System.out}
     */
    public void dump() {
        dump(System.out, 0);
    }

    /**
     * Dumps the status of this {@code View} to {@code System.out} at the given indentation level
     */
    public void dump(PrintStream out, int indent) {
        dumpFirstPart(out, indent);
        out.println("/>");
    }

    protected void dumpFirstPart(PrintStream out, int indent) {
        dumpIndent(out, indent);

        out.print("<" + realView.getClass().getSimpleName());
        if (id > 0) {
            out.print(" id=\"" + shadowOf(context).getResourceLoader().getNameForId(id) + "\"");
        }
    }

    protected void dumpIndent(PrintStream out, int indent) {
        for (int i = 0; i < indent; i++) out.print(" ");
    }

    /**
     * @return left side of the view
     */
    @Implementation
    public int getLeft() {
        return left;
    }

    /**
     * @return top coordinate of the view
     */
    @Implementation
    public int getTop() {
        return top;
    }

    /**
     * @return right side of the view
     */
    @Implementation
    public int getRight() {
        return right;
    }

    /**
     * @return bottom coordinate of the view
     */
    @Implementation
    public int getBottom() {
        return bottom;
    }

    /**
     * @return whether the view is clickable
     */
    @Implementation
    public boolean isClickable() {
        return clickable;
    }

    /**
     * Non-Android accessor.
     *
     * @return the resource ID of this views background
     */
    public int getBackgroundResourceId() {
        return backgroundResourceId;
    }

    /**
     * Non-Android accessor.
     *
     * @return whether or not {@link #invalidate()} has been called
     */
    public boolean wasInvalidated() {
        return wasInvalidated;
    }

    /**
     * Clears the wasInvalidated flag
     */
    public void clearWasInvalidated() {
        wasInvalidated = false;
    }

    /**
     * Non-Android accessor.
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * Non-Android accessor.
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * Non-Android accessor.
     */
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * Non-Android accessor.
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * Non-Android accessor.
     */
    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    /**
     * Non-Android accessor.
     */
    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    /**
     * Non-Android accessor.
     */
    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    /**
     * Non-Android accessor.
     */
    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    /**
     * Non-Android accessor.
     */
    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    /**
     * Non-Android accessor.
     *
     * @return true if this object and all of its ancestors are {@code View.VISIBLE}, returns false if this or
     *         any ancestor is not {@code View.VISIBLE}
     */
    public boolean derivedIsVisible() {
        View parent = realView;
        while (parent != null) {
            if (parent.getVisibility() != View.VISIBLE) {
                return false;
            }
            parent = (View) parent.getParent();
        }
        return true;
    }

    /**
     * Utility method for clicking on views exposing testing scenarios that are not possible when using the actual app.
     *
     * @throws RuntimeException if the view is disabled or if the view or any of its parents are not visible.
     */
    public boolean checkedPerformClick() {
        if (!derivedIsVisible()) {
            throw new RuntimeException("View is not visible and cannot be clicked");
        }
        if (!realView.isEnabled()) {
            throw new RuntimeException("View is not enabled and cannot be clicked");
        }

        return realView.performClick();
    }

    public void applyViewNodeAttributes(ViewLoader.ViewNode viewNode) {
        applyEnabledAttribute(viewNode);
        applyFocus(viewNode);
    }

    private void applyIdAttribute() {
        Integer id = attributeSet.getAttributeResourceValue("android", "id", 0);
        if (getId() == 0) {
            setId(id);
        }
    }

    private void applyVisibilityAttribute() {
        String visibility = attributeSet.getAttributeValue("android", "visibility");
        if (visibility != null) {
            if (visibility.equals("gone")) {
                setVisibility(View.GONE);
            } else if (visibility.equals("invisible")) {
                setVisibility(View.INVISIBLE);
            }
        }
    }

    private void applyEnabledAttribute(ViewLoader.ViewNode viewNode) {
        Boolean enabled = viewNode.getAttributeAsBool("android:enabled");
        if (enabled != null) {
            setEnabled(enabled);
        }
    }

    private void applyFocus(ViewLoader.ViewNode viewNode) {
        checkFocusOverride(viewNode);

        if (!anyParentHasFocus(realView)) {
            Boolean focusRequested = viewNode.getAttributeAsBool("android:focus");
            if (TRUE.equals(focusRequested) || realView.isFocusableInTouchMode()) {
                realView.requestFocus();
            }
        }
    }

    private void checkFocusOverride(ViewLoader.ViewNode viewNode) {
        if (viewNode.hasRequestFocusOverride()) {
            View root = realView;
            View parent = (View) root.getParent();
            while (parent != null) {
                root = parent;
                parent = (View) root.getParent();
            }
            root.clearFocus();
        }
    }

    private boolean anyParentHasFocus(View view) {
        while (view != null) {
            if (view.hasFocus()) return true;
            view = (View) view.getParent();
        }
        return false;
    }
}