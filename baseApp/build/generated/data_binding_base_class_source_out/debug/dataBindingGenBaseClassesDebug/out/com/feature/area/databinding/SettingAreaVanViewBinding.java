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
import com.motor.connect.feature.setting.van.SettingAreaVanViewModel;

public abstract class SettingAreaVanViewBinding extends ViewDataBinding {
  @NonNull
  public final RecyclerView recyclerView;

  @Bindable
  protected SettingAreaVanViewModel mViewModel;

  protected SettingAreaVanViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, RecyclerView recyclerView) {
    super(_bindingComponent, _root, _localFieldCount);
    this.recyclerView = recyclerView;
  }

  public abstract void setViewModel(@Nullable SettingAreaVanViewModel viewModel);

  @Nullable
  public SettingAreaVanViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static SettingAreaVanViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingAreaVanViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingAreaVanViewBinding>inflate(inflater, com.feature.area.R.layout.setting_area_van_view, root, attachToRoot, component);
  }

  @NonNull
  public static SettingAreaVanViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static SettingAreaVanViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<SettingAreaVanViewBinding>inflate(inflater, com.feature.area.R.layout.setting_area_van_view, null, false, component);
  }

  public static SettingAreaVanViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static SettingAreaVanViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (SettingAreaVanViewBinding)bind(component, view, com.feature.area.R.layout.setting_area_van_view);
  }
}
