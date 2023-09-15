## seata的四大模式



1. AT模式，只需要集成seata之后，回滚提交的操作seata帮助完成，我们需要后续处理！这种模式虽好，但是性能比TCC、SAGA模式差
2. TCC模式，全程手动，需要自己写commit、rollback的逻辑！
3. SAGA模式
4. XA模式