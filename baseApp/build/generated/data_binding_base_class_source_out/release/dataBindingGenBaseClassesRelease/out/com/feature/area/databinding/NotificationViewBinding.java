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
import com.motor.connect.feature.notification.NotificationViewModel;

public abstract class NotificationViewBinding extends ViewDataBinding {
  @NonNull
  public final ImageView actionLeft;

  @NonNull
  public final RecyclerView recyclerViewSmsReceivers;

  @NonNull
  public final TextView txtEmpty;

  @NonNull
  public final TextView txtTitle;

  @Bindable
  protected NotificationViewModel mViewModel;

  protected NotificationViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView actionLeft, RecyclerView recyclerViewSmsReceivers,
      TextView txtEmpty, TextView txtTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actionLeft = actionLeft;
    this.recyclerViewSmsReceivers = recyclerViewSmsReceivers;
    this.txtEmpty = txtEmpty;
    this.txtTitle = txtTitle;
  }

  public abstract void setViewModel(@Nullable NotificationViewModel viewModel);

  @Nullable
  public NotificationViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static NotificationViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static NotificationViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<NotificationViewBinding>inflate(inflater, com.feature.area.R.layout.notification_view, root, attachToRoot, component);
  }

  @NonNull
  public static NotificationViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static NotificationViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<NotificationViewBinding>inflate(inflater, com.feature.area.R.layout.notification_view, null, false, component);
  }

  public static NotificationViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static NotificationViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (NotificationViewBinding)bind(component, view, com.feature.area.R.layout.notification_view);
  }
}
