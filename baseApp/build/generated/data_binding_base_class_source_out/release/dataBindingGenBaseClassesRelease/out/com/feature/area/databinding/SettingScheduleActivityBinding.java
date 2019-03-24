package com.feature.area.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.motor.connect.feature.setting.schedule.SettingScheduleViewModel;

public abstract class SettingScheduleActivityBinding extends ViewDataBinding {
  @NonNull
  public final ImageView btnActionLeft;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final TextView txtEmpty;

  @NonNull
  public final TextView txtTitle;

  @Bindable
  protected SettingScheduleViewModel mViewModel;

  protected SettingScheduleActivityBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView btnActionLeft, RecyclerView recyclerView, TextView txtEmpty,
      TextView txtTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnActionLeft = btnActionLeft;
    this.recyclerView = recyclerView;
    this.txtEmpty = txtEmpty;
    this.txtTitle = txtTitle;
  }

  public abstract void setViewModel(@Nullable SettingScheduleViewModel viewModel);

  @Nullable
  public SettingScheduleViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static SettingScheduleActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingScheduleActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingScheduleActivityBinding>inflate(inflater, com.feature.area.R.layout.setting_schedule_activity, root, attachToRoot, component);
  }

  @NonNull
  public static SettingScheduleActivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingScheduleActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingScheduleActivityBinding>inflate(inflater, com.feature.area.R.layout.setting_schedule_activity, null, false, component);
  }

  public static SettingScheduleActivityBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static SettingScheduleActivityBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (SettingScheduleActivityBinding)bind(component, view, com.feature.area.R.layout.setting_schedule_activity);
  }
}
