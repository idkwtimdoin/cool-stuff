%% V, d: x, y
pts = [12.5 25 37.5 55 62.5 75; 20 59 118 197 299 420];
%% WRITTEN REGRESSION vs BUILTIN METHODS
C = QuadFit(pts(1,:), pts(2,:))     % returns 3x1 [c0; c1; c2]
disp('= c0; c1; c2');
Cb = polyfit(pts(1,:), pts(2,:), 2)     % returns 1x3 [c2 c1 c0]
disp('= c2 c1 c0');
%% MSE CALC (only for comparison)
j=0; MSEy=0; MSEyb=0;
for t = pts(1,:)
    j = j + 1;
    MSEy = MSEy + (C(3)*t^2 + C(2)*t + C(1) - pts(2,j))^2;
    MSEyb = MSEyb + (Cb(1)*t^2 + Cb(2)*t + Cb(3) - pts(2,j))^2;
    if j == length(pts(1,:))
        MSEy = MSEy/j;
        MSEyb = MSEyb/j;
    end
end
%% PLOT
t = linspace(0,100);        %<<<<------------ change plot xrange here
y = C(3)*(t.^2) + C(2)*t + C(1);
yb = Cb(1)*(t.^2) + Cb(2)*t + Cb(3);
% y & yb use QuadFit and polyfit coefficients respectively
plot(pts(1,:), pts(2,:), 'g*');
hold on;
plot(t, y, 'r');
hold on;
plot(t, yb, 'b--');
xlabel('v (m/s)');
ylabel('d (m)');
grid on;
legend('pts', 'QuadFit', 'polyfit', 'Location', 'best');
%% REGRESSION FUNCTION
function C = QuadFit(xs, ys)
x0 = 1;
x1 = sum(xs);
x2 = sum(xs.^2);
x3 = sum(xs.^3);
x4 = sum(xs.^4);
y = sum(ys);
yx = sum(xs.*ys);
yx2 = sum((xs.^2).*ys);
% initializing matrix entries and RHS vector
AtA = [x0 x1 x2; x1 x2 x3; x2 x3 x4];
AtY = [y; yx; yx2];
% solve A C = Y by reducing A size to 3x3 by multiplying by At from left
% AtA C = AtY; // At is A transposed
% C = AtA' AtY // AtA' is inverse of At*A
C = (inv(AtA))*AtY;
end