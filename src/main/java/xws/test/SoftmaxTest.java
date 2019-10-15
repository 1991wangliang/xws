package xws.test;

import xws.neuron.Tensor;
import xws.neuron.UtilNeuralNet;
import xws.neuron.layer.loss.SoftmaxLayer;

/**
 * softmax test
 * Created by xws on 2019/10/13.
 */
public class SoftmaxTest {

    public static void main(String[] args) {

        SoftmaxLayer softmaxLayer = new SoftmaxLayer();
        Tensor tensorInput = TenserUtil.createTensor(4);
        tensorInput.show("input ");
        Tensor tensorOut = softmaxLayer.forward(tensorInput);
        tensorOut.show("out ");
        FullNetWorkTest.expectMNIST(3,4);
        UtilNeuralNet.e

    }
}
