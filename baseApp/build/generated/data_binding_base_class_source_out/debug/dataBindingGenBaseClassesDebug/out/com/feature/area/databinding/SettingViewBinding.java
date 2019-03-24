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
import android.widget.TextView;
import com.motor.connect.feature.setting.SettingViewModel;

public abstract class SettingViewBinding extends ViewDataBinding {
  @NonNull
  public final ImageView btnActionLeft;

  @NonNull
  public final TextView txtAreaSetting;

  @NonNull
  public final TextView txtConfig;

  @NonNull
  public final TextView txtHelp;

  @NonNull
  public final TextView txtHowUse;

  @NonNull
  public final TextView txtNote;

  @NonNull
  public final TextView txtTitle;

  @Bindable
  protected SettingViewModel mViewModel;

  protected SettingViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView btnActionLeft, TextView txtAreaSetting, TextView txtConfig,
      TextView txtHelp, TextView txtHowUse, TextView txtNote, TextView txtTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnActionLeft = btnActionLeft;
    this.txtAreaSetting = txtAreaSetting;
    this.txtConfig = txtConfig;
    this.txtHelp = txtHelp;
    this.txtHowUse = txtHowUse;
    this.txtNote = txtNote;
    this.txtTitle = txtTitle;
  }

  public abstract void setViewModel(@Nullable SettingViewModel viewModel);

  @Nullable
  public SettingViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static SettingViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingViewBinding>inflate(inflater, com.feature.area.R.layout.setting_view, root, attachToRoot, component);
  }

  @NonNull
  public static SettingViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingViewBinding>inflate(inflater, com.feature.area.R.layout.setting_view, null, false, component);
  }

  public static SettingViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static SettingViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (SettingViewBinding)bind(component, view, com.feature.area.R.layout.setting_view);
  }
}
