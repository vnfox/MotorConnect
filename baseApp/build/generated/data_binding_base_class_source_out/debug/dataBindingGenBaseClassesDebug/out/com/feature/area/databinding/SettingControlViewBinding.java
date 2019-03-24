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
import android.widget.TextView;
import com.motor.connect.feature.setting.control.SettingControlViewModel;

public abstract class SettingControlViewBinding extends ViewDataBinding {
  @NonNull
  public final TextView btnAgenda;

  @NonNull
  public final TextView btnManual;

  @NonNull
  public final RecyclerView rcControl;

  @Bindable
  protected SettingControlViewModel mViewModel;

  protected SettingControlViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView btnAgenda, TextView btnManual, RecyclerView rcControl) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnAgenda = btnAgenda;
    this.btnManual = btnManual;
    this.rcControl = rcControl;
  }

  public abstract void setViewModel(@Nullable SettingControlViewModel viewModel);

  @Nullable
  public SettingControlViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static SettingControlViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingControlViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingControlViewBinding>inflate(inflater, com.feature.area.R.layout.setting_control_view, root, attachToRoot, component);
  }

  @NonNull
  public static SettingControlViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingControlViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingControlViewBinding>inflate(inflater, com.feature.area.R.layout.setting_control_view, null, false, component);
  }

  public static SettingControlViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static SettingControlViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (SettingControlViewBinding)bind(component, view, com.feature.area.R.layout.setting_control_view);
  }
}
