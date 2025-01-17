package xws.neuron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xws.neuron.layer.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 卷积神经网络
 * 前向传播搞定了
 * Created by xws on 2019/1/5.
 */
public class CNNetWork extends NeuralNetWork {

    private List<Layer> layers = new ArrayList<>();

    //学习率
    private double learnRate = UtilNeuralNet.e();

    //step rnn控制输入是否开始
    private int step = 0;

    //添加层到神经网络里面去
    public void addLayer(Layer layer) {
        layers.add(layer);
    }


    //是否进入学习状态
    public void entryLearn() {
        //所有层进入learn状态
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            layer.setTest(false);
        }
    }

    //是否进入工作状态
    public void entryTest() {
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            layer.setTest(true);
        }
    }

    //卷积神经网络学习
    public int learn(Tensor tensorInput, double[] expect) {
//        tensorInput.show();


        int maxIndex = 0;
        int batchSize = getBatchSize();
        for (int b = 0; b < batchSize; b++) {
            //前向传播
            work(tensorInput);

            if (expect == null) {
                continue;
            }

            //误差计算
            Layer lastLayer = layers.get(layers.size() - 1);
            lastLayer.setExpect(expect);
            //获取识别的值
            double[] result = lastLayer.a().getArray();
            maxIndex = UtilNeuralNet.maxIndex(result);
            //反向传播
            Tensor error = lastLayer.error();
            for (int i = layers.size() - 1; i >= 0; i--) {
                lastLayer = layers.get(i);
                error = lastLayer.backPropagation(error);
            }
        }

        setBatch(getBatch() + 1);//批次号自增

        return maxIndex;

    }

    //卷积神经网络工作
    public double[] work(Tensor tensorInput) {
        //前向传播
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            layer.setBatch(getBatch());
            tensorInput = layer.forward(tensorInput);
        }
        double[] result = tensorInput.getArray();
        return result;
    }

    //加载卷积神经网络从硬盘上
    public static CNNetWork load(String name) {
        JSONObject jsonObject = loadJson(name);


        CNNetWork cnNetWork = new CNNetWork();
        int version = jsonObject.getIntValue("version");
        cnNetWork.setVersion(version + 1);


        //从json中提取出各个层
        JSONArray layerArr = jsonObject.getJSONArray("layers");
        for (int i = 0; i < layerArr.size(); i++) {
//            System.out.println(layerArr.get(i));
            JSONObject layer = layerArr.getJSONObject(i);
            String strType = layer.getString("type");
            if ("filter".equals(strType)) {
                FilterLayer filterLayer = JSONObject.parseObject(layer.toString(), FilterLayer.class);
                cnNetWork.addLayer(filterLayer);
            } else if ("pool".equals(strType)) {
                PoolLayer poolLayer = JSONObject.parseObject(layer.toString(), PoolLayer.class);
                cnNetWork.addLayer(poolLayer);
            } else if ("full".equals(strType)) {
                FullLayer fullLayer = JSONObject.parseObject(layer.toString(), FullLayer.class);
                cnNetWork.addLayer(fullLayer);
            } else if ("CrossEntropyLayer".equals(strType)) {
                CrossEntropyLayer crossEntropyLayer = JSONObject.parseObject(layer.toString(), CrossEntropyLayer.class);
                cnNetWork.addLayer(crossEntropyLayer);
            } else if ("DropoutLayer".equals(strType)) {
                DropoutLayer dropoutLayer = JSONObject.parseObject(layer.toString(), DropoutLayer.class);
                cnNetWork.addLayer(dropoutLayer);
            } else if ("SoftmaxLayer".equals(strType)) {
                SoftmaxLayer softmaxLayer = JSONObject.parseObject(layer.toString(), SoftmaxLayer.class);
                cnNetWork.addLayer(softmaxLayer);
            } else if ("DepthSeparableLayer".equals(strType)) {
                DepthSeparableLayer depthSeparableLayer = JSONObject.parseObject(layer.toString(), DepthSeparableLayer.class);
                cnNetWork.addLayer(depthSeparableLayer);
            } else if ("MnLayer".equals(strType)) {
                MnLayer mnLayer = JSONObject.parseObject(layer.toString(), MnLayer.class);
                cnNetWork.addLayer(mnLayer);
            } else if ("LnLayer".equals(strType)) {
                LnLayer lnLayer = JSONObject.parseObject(layer.toString(), LnLayer.class);
                cnNetWork.addLayer(lnLayer);
            } else if ("BnLayer".equals(strType)) {
                BnLayer bnLayer = JSONObject.parseObject(layer.toString(), BnLayer.class);
                cnNetWork.addLayer(bnLayer);
            } else if ("RnnLayer".equals(strType)) {
                RnnLayer rnnLayer = JSONObject.parseObject(layer.toString(), RnnLayer.class);
                cnNetWork.addLayer(rnnLayer);
            }


        }

        return cnNetWork;
    }


    @Override
    public double totalError() {
        Layer lastLayer = layers.get(layers.size() - 1);
        //拿到期望值
        double[] expect = lastLayer.getExpect();
        double[] a = lastLayer.a().getArray();
        double totalError = 0;
        for (int i = 0; i < expect.length; i++) {
            totalError = totalError + Math.pow(expect[i] - a[i], 2);
        }
        return totalError;
    }

    public List<Layer> getLayers() {
        return layers;
    }


    public double getLearnRate() {
        return learnRate;
    }

    public void setLearnRate(double learnRate) {
        this.learnRate = learnRate;
        if (layers != null) {
            for (int i = 0; i < layers.size(); i++) {
                layers.get(i).setLearnRate(learnRate);
            }
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
        if (layers != null) {
            for (int i = 0; i < layers.size(); i++) {
                layers.get(i).setStep(step);
            }
        }
    }
}

