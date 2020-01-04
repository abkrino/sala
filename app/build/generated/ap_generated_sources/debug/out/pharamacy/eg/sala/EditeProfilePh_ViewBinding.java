// Generated code from Butter Knife. Do not modify!
package pharamacy.eg.sala;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditeProfilePh_ViewBinding implements Unbinder {
  private EditeProfilePh target;

  private View view7f090110;

  @UiThread
  public EditeProfilePh_ViewBinding(EditeProfilePh target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditeProfilePh_ViewBinding(final EditeProfilePh target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.profile_pic, "method 'onProfileImageClick'");
    view7f090110 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onProfileImageClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f090110.setOnClickListener(null);
    view7f090110 = null;
  }
}
