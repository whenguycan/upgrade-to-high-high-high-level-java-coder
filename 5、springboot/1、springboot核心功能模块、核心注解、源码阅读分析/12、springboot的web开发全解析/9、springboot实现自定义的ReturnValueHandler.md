## springboot实现自定义的ReturnValueHandler

用系统内预置的就够了，不用自己实现。



就说下ReturnValueHandler实现类中两个方法的作用吧

- supportsReturnType方法， 判断当前ReturnValueHandler是否能够使用，返回true是可以使用，返回false是不可以使用
- handleReturnValue方法，一般在这里面直接处理HttpServletResponse，把需要的数据填到HttpServletResponse中就行了



<font color="red">自定义的ReturnValueHandler需要通过WebMvcConfigurer加入到应用中，才能使用</font>