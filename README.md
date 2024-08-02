## 为了能让更多的开发者开发出更多有特点的权杖，或者想要专门为自己玩家皮肤的头颅制作自己专属功能的权杖，本模组还添加了 3 个相关的事件，用来注册不同的功能：

### 1. RegisterNormalStaffFunctionEvent：给一个带有普通方块的权杖注册新的功能，该事件提供了一个注册方法。其中 "block" 参数为方块的对象；"function" 参数为方块的功能，该参数必须是一个实现了 INormalStaffFunction 接口的类对象；

### 2. RegisterPlayerHeadStaffFunctionEvent：给一个带有自定义玩家头颅的权杖注册新的功能，该事件提供了一个注册方法。其中 "block" 参数为方块的对象；"function" 参数为方块的功能，该参数必须是一个实现了 IPlayerHeadStaffFunction 接口的类对象；

### 3. RegisterStaffCoreBlockRenderEvent：部分带有特殊渲染的实体方块无法直接在权杖上显示出来（比如原版的钟、饰纹陶罐、末地折跃门等），可以通过这个事件进行处理，具体的参数可以直接去查看这个事件的类。
