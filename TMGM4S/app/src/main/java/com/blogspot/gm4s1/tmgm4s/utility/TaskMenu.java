package com.blogspot.gm4s1.tmgm4s.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.view.menu.MenuBuilder;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.gm4s1.tmgm4s.R;

/**
 * Created by GloryMaker on 1/22/2017.
 */

public class TaskMenu implements ContextMenu {
    private MenuBuilder _menuBuilder;
    private TextView _headerTextView;
    private Context _context;

    public TaskMenu(Context context) {
         _menuBuilder = new MenuBuilder(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        params.setMargins(3, 3, 3, 3);

        _headerTextView = new TextView(context);
        _headerTextView.setLayoutParams(params);
        _headerTextView.setGravity(Gravity.CENTER);
    }

    @Override
    public ContextMenu setHeaderTitle(int titleRes) {
        _headerTextView.setText(_context.getString(titleRes));
        return this;
    }
    @Override
    public ContextMenu setHeaderTitle(CharSequence title) {
        _headerTextView.setText(title);
        return this;
    }
    @Override
    public ContextMenu setHeaderIcon(int iconRes) {
        _headerTextView.setBackgroundResource(iconRes);
        return this;
    }
    @Override
    public ContextMenu setHeaderIcon(Drawable icon) {
        //_headerTextView.setBackground(icon);
        return this;
    }
    @Override
    public ContextMenu setHeaderView(View view) {

        return this;
    }



    @Override
    public void clearHeader() {
        _menuBuilder.clearHeader();
    }
    @Override
    public MenuItem add(CharSequence title) {
        return _menuBuilder.add(title);
    }
    @Override
    public MenuItem add(int titleRes) {
        return _menuBuilder.add(titleRes);
    }
    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        return _menuBuilder.add(groupId, itemId, order, title);
    }
    @Override
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return _menuBuilder.add(groupId, itemId, order, titleRes);
    }
    @Override
    public SubMenu addSubMenu(CharSequence title) {
        return _menuBuilder.addSubMenu(title);
    }
    @Override
    public SubMenu addSubMenu(int titleRes) {
        return _menuBuilder.addSubMenu(titleRes);
    }
    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        return _menuBuilder.addSubMenu(groupId, itemId, order, title);
    }
    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return _menuBuilder.addSubMenu(groupId, itemId, order, titleRes);
    }
    @Override
    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        return _menuBuilder.addIntentOptions(groupId, itemId, order, caller, specifics, intent, flags, outSpecificItems);
    }
    @Override
    public void removeItem(int id) {
        _menuBuilder.removeItem(id);
    }
    @Override
    public void removeGroup(int groupId) {
        _menuBuilder.removeGroup(groupId);
    }
    @Override
    public void clear() {
        _menuBuilder.clear();
    }
    @Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        _menuBuilder.setGroupCheckable(group, checkable, exclusive);
    }
    @Override
    public void setGroupVisible(int group, boolean visible) {
        _menuBuilder.setGroupVisible(group, visible);
    }
    @Override
    public void setGroupEnabled(int group, boolean enabled) {
        _menuBuilder.setGroupEnabled(group, enabled);
    }
    @Override
    public boolean hasVisibleItems() {
        return _menuBuilder.hasVisibleItems();
    }
    @Override
    public MenuItem findItem(int id) {
        return _menuBuilder.findItem(id);
    }
    @Override
    public int size() {
        return _menuBuilder.size();
    }
    @Override
    public MenuItem getItem(int index) {
        return _menuBuilder.getItem(index);
    }
    @Override
    public void close() {
        _menuBuilder.close();
    }
    @Override
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        return _menuBuilder.performShortcut(keyCode, event, flags);
    }
    @Override
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return _menuBuilder.isShortcutKey(keyCode, event);
    }
    @Override
    public boolean performIdentifierAction(int id, int flags) {
        return _menuBuilder.performIdentifierAction(id, flags);
    }
    @Override
    public void setQwertyMode(boolean isQwerty) {
        _menuBuilder.setQwertyMode(isQwerty);
    }
}
