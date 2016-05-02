/**
* Copyright (c) Acroquest Technology Co, Ltd. All Rights Reserved.
* Please read the associated COPYRIGHTS file for more details.
*
* THE SOFTWARE IS PROVIDED BY Acroquest Technolog Co., Ltd.,
* WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
* BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
* CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
* OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/
package acromusashi.stream.ml.clustering.kmeans;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acromusashi.stream.ml.clustering.kmeans.entity.KmeansPoint;

/**
 * KMeansの対象ポイントを生成するFunctionクラス
 * 
 * @author kimura
 */
public class KmeansCreator extends BaseFunction
{
    /** serialVersionUID */
    private static final long   serialVersionUID = 4620883521921594615L;

    /** logger */
    private static final Logger logger           = LoggerFactory.getLogger(KmeansCreator.class);

    /** 読みこんだデータを分割する文字列 */
    private String              delimeter        = ",";

    /**
     * パラメータを指定せずにインスタンスを生成する。
     */
    public KmeansCreator()
    {}

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map conf, TridentOperationContext context)
    {
        if (logger.isDebugEnabled() == true)
        {
            logger.debug("prepared started. taskIndex=" + context.getPartitionIndex() + ", taxkNum="
                    + context.numPartitions());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector)
    {
        String receivedStr = tuple.getString(0);

        String[] splitedStr = StringUtils.split(receivedStr, this.delimeter);
        int dataNum = splitedStr.length;
        double[] points = new double[splitedStr.length];

        try
        {
            for (int index = 0; index < dataNum; index++)
            {
                points[index] = Double.parseDouble(splitedStr[index].trim());
            }

            KmeansPoint result = new KmeansPoint();
            result.setDataPoint(points);
            collector.emit(new Values(result));
        }
        catch (Exception ex)
        {
            logger.warn("Received data is invalid. skip this data. ReceivedData=" + receivedStr,
                    ex);
        }
    }

    /**
     * @return the delimeter
     */
    public String getDelimeter()
    {
        return this.delimeter;
    }

    /**
     * @param delimeter the delimeter to set
     */
    public void setDelimeter(String delimeter)
    {
        this.delimeter = delimeter;
    }
}
