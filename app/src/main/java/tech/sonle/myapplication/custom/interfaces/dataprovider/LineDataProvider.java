package tech.sonle.myapplication.custom.interfaces.dataprovider;

import tech.sonle.myapplication.custom.components.YAxis;
import tech.sonle.myapplication.custom.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
