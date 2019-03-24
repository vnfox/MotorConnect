package com.feature.area.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.motor.connect.feature.setting.area.SettingAreaScheduleViewModel;

public abstract class SettingAreaScheduleViewBinding extends ViewDataBinding {
  @NonNull
  public final ImageView btnClose;

  @NonNull
  public final LinearLayout container;

  @NonNull
  public final RadioButton rdSchedule1;

  @NonNull
  public final RadioButton rdScheduleLoopNone;

  @NonNull
  public final LinearLayout secondContainer;

  @NonNull
  public final LinearLayout thirdContainer;

  @NonNull
  public final TextView txtTime1Run;

  @NonNull
  public final TextView txtTime1Start;

  @NonNull
  public final TextView txtTime2Run;

  @NonNull
  public final TextView txtTime2Start;

  @NonNull
  public final TextView txtTime3Run;

  @NonNull
  public final TextView txtTime3Start;

  @Bindable
  protected SettingAreaScheduleViewModel mViewModel;

  protected SettingAreaScheduleViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView btnClose, LinearLayout container, RadioButton rdSchedule1,
      RadioButton rdScheduleLoopNone, LinearLayout secondContainer, LinearLayout thirdContainer,
      TextView txtTime1Run, TextView txtTime1Start, TextView txtTime2Run, TextView txtTime2Start,
      TextView txtTime3Run, TextView txtTime3Start) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnClose = btnClose;
    this.container = container;
    this.rdSchedule1 = rdSchedule1;
    this.rdScheduleLoopNone = rdScheduleLoopNone;
    this.secondContainer = secondContainer;
    this.thirdContainer = thirdContainer;
    this.txtTime1Run = txtTime1Run;
    this.txtTime1Start = txtTime1Start;
    this.txtTime2Run = txtTime2Run;
    this.txtTime2Start = txtTime2Start;
    this.txtTime3Run = txtTime3Run;
    this.txtTime3Start = txtTime3Start;
  }

  public abstract void setViewModel(@Nullable SettingAreaScheduleViewModel viewModel);

  @Nullable
  public SettingAreaScheduleViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static SettingAreaScheduleViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingAreaScheduleViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingAreaScheduleViewBinding>inflate(inflater, com.feature.area.R.layout.setting_area_schedule_view, root, attachToRoot, component);
  }

  @NonNull
  public static SettingAreaScheduleViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingAreaScheduleViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingAreaScheduleViewBinding>inflate(inflater, com.feature.area.R.layout.setting_area_schedule_view, null, false, component);
  }

  public static SettingAreaScheduleViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static SettingAreaScheduleViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (SettingAreaScheduleViewBinding)bind(component, view, com.feature.area.R.layout.setting_area_schedule_view);
  }
}
