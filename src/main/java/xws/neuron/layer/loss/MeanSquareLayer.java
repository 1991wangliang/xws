package xws.neuron.layer.loss;

import xws.neuron.Tensor;
import xws.neuron.layer.Layer;


/**
 * prev layer must full layer
 * 上一个神经层必须是全连接层
 * Created by xws on 2019/2/20.
 */
public class MeanSquareLayer extends Layer {


    private Tensor tensorInput;

    //每次计算之前必须清空
    private Tensor tensorPda;//∂C/∂A


    public MeanSquareLayer() {
    }

    //初始化神经网络层,num为神经元的数量，inputs为输入的数量
    public MeanSquareLayer(String name) {
        super("MeanSquareLayer");
        setName(name);
    }


    //计算每一个神经元的输出值
    @Override
    public Tensor forward(Tensor tensor) {
        tensorInput = tensor;
        return tensor;
    }

    //获取这一层神经网络的输出
    @Override
    public Tensor a() {
        return tensorInput;
    }


    /**
     * 反向传播
     * 先计算计算∂C/∂I
     * 再计算∂C/∂W
     * 再计算∂C/∂B
     */
    @Override
    public Tensor backPropagation(Tensor tensor) {
        return tensor;
    }

    //误差计算
    @Override
    public Tensor error() {


        tensorPda = new Tensor();
        tensorPda.setDepth(1);
        tensorPda.setHeight(1);
        tensorPda.setWidth(tensorInput.getWidth());

        for (int i = 0; i < tensorPda.getWidth(); i++) {
            tensorPda.set(i, tensorInput.get(i) - getExpect()[i]);
        }

        return tensorPda;
    }


}
