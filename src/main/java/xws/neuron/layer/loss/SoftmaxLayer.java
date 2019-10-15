package xws.neuron.layer.loss;

import xws.neuron.Tensor;
import xws.neuron.layer.Layer;


/**
 * SoftMax
 * prev layer must full layer
 * 上一个神经层必须是全连接层
 * Created by xws on 2019/10/13.
 */
public class SoftmaxLayer extends Layer {


    private Tensor tensorInput;
    private Tensor tensorOut;

    public SoftmaxLayer() {

    }

    //初始化神经网络层,num为神经元的数量，inputs为输入的数量
    public SoftmaxLayer(int num) {
        super("SoftmaxLayer");
    }

    public SoftmaxLayer(String name, int num) {
        super("SoftmaxLayer");
        setName(name);

    }


    //获取数组最大值
    public double max(double[] array) {
        double max = 0;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    //计算每一个神经元的输出值
    @Override
    public Tensor forward(Tensor tensor) {

        tensorInput = tensor;
        tensorOut = new Tensor();
        tensorOut.setDepth(1);
        tensorOut.setHeight(1);
        tensorOut.setWidth(tensorInput.getWidth());
        tensorOut.createArray();

        double max = max(tensorInput.getArray());

        //求分子numerator
        for (int i = 0; i < tensorInput.getWidth(); i++) {
            tensorOut.set(i, Math.exp(tensorInput.get(i) - max));
        }
        //求分母denominator
        double denominator = 0;
        for (int i = 0; i < tensorOut.getWidth(); i++) {
            denominator = denominator + tensorOut.get(i);
        }
        //输出结果
        for (int i = 0; i < tensorOut.getWidth(); i++) {
            tensorOut.set(i, tensorOut.get(i) / denominator);
        }

        return tensorOut;
    }

    //获取这一层神经网络的输出
    @Override
    public Tensor a() {
        return tensorOut;
    }


    /**
     * 反向传播
     */
    @Override
    public Tensor backPropagation(Tensor tensor) {
        return tensor;
    }

    //误差计算
    @Override
    public Tensor error() {
        Tensor tensorError = new Tensor();
        tensorError.setDepth(1);
        tensorError.setHeight(1);
        tensorError.setWidth(tensorInput.getWidth());
        tensorError.createArray();

        for (int i = 0; i < tensorError.getHeight(); i++) {
            if (getExpect()[i] == 1) {
//                pda[i] = a[i] - 1;
                tensorError.set(i, tensorOut.get(i) - 1);
            } else {
//                pda[i] = a[i];
                tensorError.set(i, tensorOut.get(i));
            }
        }


        return tensorOut;
    }


}
