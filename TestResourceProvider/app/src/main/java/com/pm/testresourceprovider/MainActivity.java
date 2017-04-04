package com.pm.testresourceprovider;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pm.skinlibrary.skin.ResourceManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private final String mSkinPluginPath = Environment.getExternalStorageDirectory() +
            File.separator + "app-debug.apk";
    private final String mSkinPluginPkg = "com.pm.resourceprovider";

    private TextView title;
    private ImageView icon;
    private LinearLayout activitymain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_skin_local) {
            // TODO: 2017/3/11
            return true;
        }
        if (id == R.id.action_skin_plugin) {
            // TODO: 2017/3/11
            loadPlugin(mSkinPluginPath,mSkinPluginPkg);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPlugin(String skinPluginPath, String skinPluginPkg) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinPluginPath);
            Resources superResources =getResources();
            Resources resources = new Resources(assetManager, superResources.getDisplayMetrics(),
                    superResources.getConfiguration());
            ResourceManager mResourcesManager = new ResourceManager(skinPluginPkg, resources,null);
            //读取插件包中的对应Drawable文件名，注意没有扩展名。
            String drawableName = "skin_icon";
            Drawable drawableByRes = null;
            drawableByRes = mResourcesManager.getMipmapByResName(drawableName);
            if (drawableByRes != null) {
                // TODO: 2017/3/11
                icon.setImageDrawable(drawableByRes);
            }

            title.setTextColor(mResourcesManager.getColorByResName("skin_label_text_color"));

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {

        title = (TextView) findViewById(R.id.title);
        icon = (ImageView) findViewById(R.id.icon);
        activitymain = (LinearLayout) findViewById(R.id.activity_main);
    }
}
