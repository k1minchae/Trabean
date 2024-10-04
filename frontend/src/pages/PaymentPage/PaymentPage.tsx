import React, { useRef, useEffect } from "react";
import QrScanner from "qr-scanner";
import TopBar from "../../components/TopBar";

const PaymentPage: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement | null>(null);

  const handleScan = (result: QrScanner.ScanResult) => {
    try {
      const parsedData = JSON.parse(result.data);
      console.log(parsedData);
    } catch (error) {
      console.error("Error parsing QR code data:", error);
    }
  };

  // QrOptions 정의
  const QrOptions = {
    highlightScanRegion: true, // 스캔 영역 하이라이트 설정
    highlightCodeOutline: true, // QR 코드 윤곽 하이라이트 설정
  };

  useEffect(() => {
    const videoElem = videoRef.current;
    if (videoElem) {
      const qrScanner = new QrScanner(
        videoElem,
        (result) => handleScan(result),
        QrOptions,
      );
      qrScanner.start();

      return () => qrScanner.destroy();
    }
    return undefined;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="flex flex-col items-center h-dvh justify-center">
      <TopBar isWhite isLogo={false} page="QR 결제" />
      <video
        ref={videoRef}
        style={{
          width: "250px",
          height: "250px",
          border: "3px solid #6FA760",
          borderRadius: "15px",
        }}
        autoPlay
      >
        <track kind="captions" src="" label="No captions needed" />
      </video>
      <p className="mt-[20px] font-semibold">
        카메라 중앙에 QR 코드를 인식해주세요 !
      </p>
    </div>
  );
};

export default PaymentPage;
