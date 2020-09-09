var wsconn=new WebSocket("ws://aogege.com");
var statusdiv=document.getElementById("status");
var input=document.getElementById("mymessage");
var newmessage=document.getElementById("newmessage");
wsconn.onopen=function (ev) {
    if(wsconn.readyState==1){
        statusdiv.innerText="Connected.";
    }
};
wsconn.onclose=(ev)=>statusdiv.innerText=(wsconn.readyState>1)?"Closed.":statusdiv.innerText;

wsconn.onmessage=function(ev){
    newmessage.innerText=ev.data;
};

document.getElementById("sendbutton").onclick=function sendMessage(ev){
    if(wsconn.readyState==1){
        wsconn.send(input.value);
    }
};