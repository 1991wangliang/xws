package xws.test;

import xws.neuron.Tensor;

/**
 * tenser util
 * Created by xws on 2019/10/13.
 */
public class TenserUtil {

    //    创建一个连续数字组成的tensor
    public static Tensor createTensor(int num) {
        Tensor tensor = new Tensor();
        tensor.setDepth(1);
        tensor.setHeight(1);
        tensor.setWidth(num);
        tensor.createArray();
        for (int i = 0; i < num; i++) {
            tensor.set(i, i + 1);
        }
        return tensor;
    }


}
