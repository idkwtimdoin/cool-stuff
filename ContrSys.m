%%
z=1;
t = 0:1*z:2*864*z;
w0 = 7.28E-5;
Td = 1e-4*cos(w0*t);
I = 400;
G = tf([0 0 1],[I 0 0]);
k1 = tf([0.1 0.01],[1 0]);
k2 = tf([240 3],[80 20]);
k3 = tf([1400 35],[40 80]);
sys1 = feedback(series(k1, G), 1);
sys2 = feedback(series(k2, G), 1);
sys3 = feedback(series(k3, G), 1);

%% Stability
figure(1)
pzmap(sys1, sys2, sys3);
legend('k1','k2','k3')


Step response
[y2, T2] = step(sys2, t);
[y3, T3] = step(sys3, t);
plot(T2,y2,T3,y3)
title('Step Response')
legend('k2','k3')

S2 = stepinfo(sys2);
S3 = stepinfo(sys3);


Disturbance input
sysd2 = feedback(G, k2);
sysd3 = feedback(G, k3);
dist2 = (180/pi)*lsim(sysd2, Td, t);
dist3 = (180/pi)*lsim(sysd3, Td, t);
plot(t,dist2, t,dist3)
legend('k2','k3')

Reference step input
opt = stepDataOptions('StepAmplitude', 5*pi/180);
[yr2, Tr2] = step(sys2, opt, t);
plot(Tr2, yr2)

Control Torque response to Td
syst = series(-k2, G);
[y2,t2] = lsim(syst, Td, t);
plot(t2,y2)

Control Torque response to step ref
sysT = feedback(k2, G);
opt = stepDataOptions('StepAmplitude', 5*pi/180);
[yT2, TT2] = step(sys2, opt, t);
plot(TT2, yT2)
grid on;

%%
G = tf([0 0 -1],[-I 0 0]);
sys = feedback( series(G, k1), 1, -1);
[y,T] = lsim(sys, Td, t);
subplot(3,1,1)
plot(T, y);
title('k1');
sys = feedback( series(G, k2), 1, -1);
[y1,T1] = lsim(sys, Td, t);
subplot(3,1,2)
plot(T1, y1);
title('k2');
sys = feedback( series(G, k3), 1, -1);
[y2,T2] = lsim(sys, Td, t);
subplot(3,1,3)
plot(T2, y2);
title('k3');

%% Reference step input 
opt = stepDataOptions('StepAmplitude', 5*pi/180);
[yr2, Tr2] = step(sys1, opt, t);
plot(Tr2, yr2)



