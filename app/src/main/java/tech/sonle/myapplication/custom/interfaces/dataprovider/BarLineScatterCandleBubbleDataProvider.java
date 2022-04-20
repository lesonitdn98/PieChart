package tech.sonle.myapplication.custom.interfaces.dataprovider;

import tech.sonle.myapplication.custom.components.YAxis.AxisDependency;
import tech.sonle.myapplication.custom.data.BarLineScatterCandleBubbleData;
import tech.sonle.myapplication.custom.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
