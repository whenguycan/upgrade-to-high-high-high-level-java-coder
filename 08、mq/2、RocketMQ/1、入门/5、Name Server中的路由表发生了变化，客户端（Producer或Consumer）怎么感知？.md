## Name Server中的路由表发生了变化，客户端（Producer或Consumer）怎么感知？

当Name Server中路由信息出现变化时，NameServer不会主动推送给客户端，而是客户端（Producer/Consumer端）定时拉取Topic最新的路由。默认客端每30秒会拉取一次最新的路由。