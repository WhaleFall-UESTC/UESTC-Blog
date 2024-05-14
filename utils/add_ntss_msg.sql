use uestc_blog

insert into msgs (id, msg, userId) values (1, "交朋友就要交那种变成老头还能相互称呼绰号的", 1);
insert into msgs (id, msg, userId) values (2, "能吃的时候就多吃点", 2);
insert into msgs (id, msg, userId) values (3, "大家都秃了就没有秃子了", 1);
insert into ntss (id, nts) values (1, "我对普通的人类没有兴趣。你们之中如果有外星人、未来人、异世界来客或者超能力者的话就来找我，完毕");

insert into users (name, password, email, authority) values
    ("admin", "admin", "admin@blog.com", 3);
insert into users (name, password, email, authority) values
    ("123", "123", "123@456.com", 1);
