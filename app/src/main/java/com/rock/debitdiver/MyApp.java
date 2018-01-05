package com.rock.debitdiver;

import android.app.Application;

import com.rock.debitdiver.Model.DebtInfo;

import java.util.ArrayList;

/**
 * Created by michalejackson on 12/29/17.
 */

public class MyApp extends Application {
    public ArrayList<DebtInfo> debtLists = new ArrayList<>();
}
