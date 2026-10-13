import {Game} from './engine.js';
const game=new Game(), canvas=document.querySelector('canvas'),ctx=canvas.getContext('2d');
const overlay=document.querySelector('#overlay'),held=new Set();let touch=0,last=0,shown='';
const stars=Array.from({length:65},(_,i)=>[(i*137+31)%800,90+(i*79)%450]);
const sprites={ship:['  rr      ',' rrrr     ',' rwwwr    ','wwwwwccw  ','wwwwwwwwww','wwwwwccw  ',' rwwwr    ',' rrrr     ','  rr      '],station:['       ww       ','       ww       ','bbb  wwwwww  bbb','bbbwwwwggwwwwbbb','bbb  wwggww  bbb','bbb  wwwwww  bbb','      wwww      ','       gg       ','       gg       '],cell:[' www ','wgggw','wgggw','wgggw',' www ']};
function sprite(kind,x,y,scale,state){const a=sprites[kind];for(let row=0;row<a.length;row++)for(let col=0;col<a[row].length;col++){
const c=a[row][col];if(c===' ')continue;ctx.fillStyle=({w:'#d8e1e8',r:'#c86b75',b:'#446584',c:'#78c9e1',g:state||'#8cc9a7'})[c];ctx.fillRect(Math.round(x)+col*scale,Math.round(y)+row*scale,scale,scale);}}
function sync(){const phase=game.phase;overlay.hidden=phase==='playing';document.querySelector('#difficulty').hidden=phase!=='ready';document.querySelector('#pause').textContent=phase==='paused'?'Resume':'Pause';
if(phase==='ready'){document.querySelector('#title').textContent='Rescue the station';document.querySelector('#intro').textContent='Three deliveries. Five fuel cells. Make them count.';document.querySelector('#play').textContent='Play';}
else if(phase==='paused'){document.querySelector('#title').textContent='Paused';document.querySelector('#intro').textContent='Ready when you are.';document.querySelector('#play').textContent='Resume';}
else if(phase==='won'||phase==='lost'){document.querySelector('#title').textContent=phase==='won'?'Station rescued!':'Try again';document.querySelector('#intro').textContent=game.message+' Score: '+game.score;document.querySelector('#play').textContent='Play again';}
if(shown!==game.message){shown=game.message;document.querySelector('#status').textContent=shown;}}
function draw(){ctx.fillStyle='#101522';ctx.fillRect(0,0,800,600);ctx.fillStyle='#344358';for(const [x,y]of stars)ctx.fillRect(x,y,2,2);
ctx.fillStyle='#1c2738';ctx.fillRect(0,0,800,70);ctx.fillRect(0,564,800,36);ctx.font='bold 17px monospace';ctx.fillStyle='#dce3ed';ctx.fillText(game.message.toUpperCase(),20,27);ctx.fillStyle='#8cc9a7';ctx.fillText(`ROUND ${game.round}/3    FUEL ${game.fuel}/5    SCORE ${game.score}`,20,54);
sprite('ship',25,game.shipY-18,4);sprite('station',game.x-32,game.y-18,4,game.orbit===0?'#8cc9a7':game.orbit===1?'#e5c56c':'#d47777');if(game.cell)sprite('cell',game.cell.x-10,game.cell.y-10,4);
ctx.font='14px monospace';ctx.fillStyle='#99a9bd';ctx.fillText('K/J MOVE    SPACE FIRE    P PAUSE    R RESTART',20,587);}
function step(now){game.update(last?(now-last)/1000:0,touch||((held.has('ArrowDown')||held.has('j')?1:0)-(held.has('ArrowUp')||held.has('k')?1:0)));last=now;sync();draw();requestAnimationFrame(step);}
function restart(){held.clear();touch=0;game.reset();sync();}
document.querySelectorAll('[data-level]').forEach(b=>b.addEventListener('click',()=>{game.difficulty=Number(b.dataset.level);document.querySelectorAll('[data-level]').forEach(o=>o.setAttribute('aria-pressed',String(o===b)));}));
document.querySelector('#play').addEventListener('click',()=>{if(game.phase==='paused')game.pause();else{if(game.phase!=='ready')game.reset();game.start();}sync();});
document.querySelector('#pause').addEventListener('click',()=>{game.pause();sync();});document.querySelector('#restart').addEventListener('click',restart);
document.querySelector('#fullscreen').addEventListener('click',async()=>{try{if(document.fullscreenElement)await document.exitFullscreen();else await document.querySelector('main').requestFullscreen();}catch{document.querySelector('#status').textContent='Fullscreen is unavailable in this browser.';}});
document.addEventListener('fullscreenchange',()=>document.querySelector('#fullscreen').textContent=document.fullscreenElement?'Exit fullscreen':'Fullscreen');
window.addEventListener('keydown',e=>{if(e.target instanceof HTMLButtonElement&&[' ','Enter'].includes(e.key))return;const key=e.key.length===1?e.key.toLowerCase():e.key;if(['ArrowUp','ArrowDown',' '].includes(key))e.preventDefault();held.add(key);if(e.repeat)return;if(key===' ')game.fire();if(key==='p')game.pause();if(key==='r')restart();if(key==='s')game.start();if(key==='Escape'&&game.phase==='playing')game.pause();sync();});
window.addEventListener('keyup',e=>held.delete(e.key.length===1?e.key.toLowerCase():e.key));
window.addEventListener('blur',()=>{held.clear();touch=0;if(game.phase==='playing')game.pause();sync();});
for(const [id,direction]of [['up',-1],['down',1]]){const b=document.querySelector('#'+id);b.addEventListener('pointerdown',e=>{touch=direction;b.setPointerCapture(e.pointerId);});for(const event of ['pointerup','pointercancel','lostpointercapture'])b.addEventListener(event,()=>touch=0);}
document.querySelector('#fire').addEventListener('click',()=>game.fire());requestAnimationFrame(step);
