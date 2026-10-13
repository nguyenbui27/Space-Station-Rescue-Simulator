export const SPEEDS = [[100,45],[150,75],[200,110]];
export class Game {
  constructor(random = Math.random) { this.random=random; this.difficulty=0; this.reset(); }
  reset() { this.round=1; this.score=0; this.fuel=5; this.shipY=300; this.cell=null; this.phase='ready'; this.message='Refuel the station three times.'; this.orbit=0; this.newOrbit(); }
  newOrbit() { this.baseX=200+this.random()*180; this.baseY=110; this.x=this.baseX; this.y=this.baseY; this.orbit=0; }
  start() { if(this.phase==='ready') this.phase='playing'; }
  pause() { if(this.phase==='playing')this.phase='paused';else if(this.phase==='paused')this.phase='playing'; }
  fire() { if(this.phase!=='playing'||this.cell||!this.fuel)return false;this.fuel--;this.cell={x:65,y:this.shipY};this.message='Fuel cell away.';return true; }
  rescue() {
    this.score+=100*this.round+20*this.fuel;this.cell=null;
    if(this.round===3){this.phase='won';this.message='Station rescued!';return;}
    this.round++;this.fuel=Math.min(5,this.fuel+2);this.newOrbit();this.message='Nice shot! +2 fuel. Next round is faster.';
  }
  update(dt, direction=0) {
    if(this.phase!=='playing')return;
    dt=Math.max(0,Math.min(.05,dt));this.shipY=Math.max(90,Math.min(540,this.shipY+direction*250*dt));
    const ox=this.x,oy=this.y;
    this.x+=(SPEEDS[this.difficulty][0]+(this.round-1)*35)*dt;
    this.y+=(SPEEDS[this.difficulty][1]+(this.round-1)*20)*dt;
    let wrapped=false;
    if(this.x>790){this.baseX=Math.max(140,this.baseX-40);this.baseY+=50;this.x=this.baseX;this.y=this.baseY;this.orbit++;wrapped=true;}
    if(this.cell){const c=this.cell,ax=c.x-ox,ay=c.y-oy;c.x+=500*dt;
      const dx=c.x-this.x-ax,dy=c.y-this.y-ay;
      const t=Math.max(0,Math.min(1,-(ax*dx+ay*dy)/Math.max(.001,dx*dx+dy*dy)));
      if(!wrapped&&Math.hypot(ax+t*dx,ay+t*dy)<26){this.rescue();return;}
      if(c.x>820){this.cell=null;this.message='Missed. Aim a little ahead.';}}
    if(this.y>550||(!this.fuel&&!this.cell)){this.phase='lost';this.message=this.fuel?'The station drifted away.':'No fuel cells left.';}
  }
}
